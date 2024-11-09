package rand.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rand.api.domain.common.repository.InMemRepository;
import rand.api.domain.common.util.mail.RandomGenerator;
import rand.api.domain.member.model.Members;
import rand.api.domain.member.repository.MemberRepository;
import rand.api.domain.utilservice.MailService;
import rand.api.web.dto.common.ResponseDTO;
import rand.api.web.dto.member.request.*;
import rand.api.web.dto.member.response.ResFindIdDTO;
import rand.api.web.exception.custom.BadRequestException;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final InMemRepository inMemRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    private static final int MAX_ATTEMPT=5;

    @Override
    public ResponseEntity<ResponseDTO<Void>> join(JoinDTO joinDTO) {
        //패스워드 암호화
        String bcryptPwd = bCryptPasswordEncoder.encode(joinDTO.getPassword());
        joinDTO.setPassword(bcryptPwd);

        //엔티티 변환
        Members members = new Members(joinDTO);

      String historySaveKey = members.getEmail()+":authHs"; //이메일 인증이력
        
      String emailAuthHistory= (String) inMemRepository.getValue(historySaveKey);
        
      if(emailAuthHistory==null){
          throw new BadRequestException("ERR-EAUTH-CS-05"); //이메일 인증완료를 통한 레디스 이력이 없을경우

      }
      //이메일 중복 체크
      int emlCount = memberRepository.emailDuplicateCheck(members);
      //인증 후 회원가입 폼 내 그사이 동일 이메일로 가입한 경우
      if(emlCount>0){
          throw new BadRequestException("ERR-JOIN-CS-01"); //이메일 중복 에러
      }
        // 유저네임 (아이디) 중복 체크
      int usrCount = memberRepository.userNameDuplicateCheck(members);

      if(usrCount>0){
          throw new BadRequestException("ERR-JOIN-CS-03"); //아이디 중복 에러
      }
        //닉네임 중복 체크
      int nnmCount = memberRepository.nickNameDuplicateCheck(members);

      if(nnmCount>0){
          throw new BadRequestException("ERR-JOIN-CS-02"); //닉네임 중복 에러
      }

      //저장
      memberRepository.join(members);

      //이메일 인증이력 삭제
      inMemRepository.delete(historySaveKey);



        return ResponseEntity.ok(new ResponseDTO<Void>(null));
    }

    @Override
    public ResponseEntity<ResponseDTO<ResFindIdDTO>> findId(FindIdDTO findIdDTO) {
        Members members = new Members(findIdDTO);

        ResFindIdDTO resFindIdDTO = memberRepository.findId(members);
        if(resFindIdDTO==null){
            throw new BadRequestException("ERR-EAUTH-CS-06"); //등록된 아이디가 없다는 예외
        }


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO<ResFindIdDTO>(resFindIdDTO));

    }

    @Override
    public ResponseEntity<ResponseDTO<Void>> emailAuthSend(EmailAuthSendDTO emailAuthDTO) {
        //dto - > entity
        Members members= new Members(emailAuthDTO);

        //중복확인
        int presentCount = memberRepository.emailDuplicateCheck(members);

        if(presentCount !=0){
            // 이메일 중복 에러
            throw new BadRequestException("ERR-JOIN-CS-01");
        }


        //인증번호 전송
        //인증번호 레디스에 저장  인증번호 키값:이메일  밸류 = 인증번호
        mailService.emailSend(members);
        // 다시보낼시 덮어쓰기  TTL 3분

        return ResponseEntity.ok(new ResponseDTO<Void>(null));

    }

    @Override
    public ResponseEntity<ResponseDTO<Void>> resetPwd(ResetPwdDTO resetPwdDTO) {
        Members members = new Members(resetPwdDTO);

        //정보 기반 validation

        int resCount = memberRepository.findByNnAndEmail(members);

        //입력된 정보가 없음

        if(resCount==0){
           throw new BadRequestException("");
       }

        //비밀번호 업데이트
        String password = RandomGenerator.generateRandomPassword();//임시패스워드 생성

        String securedPassword= bCryptPasswordEncoder.encode(password);//데이터베이스에 암호화 해서 저장할 패스워드
        members.setPassword(securedPassword);// 새로운 임시비밀번호 저장
        memberRepository.resetNewPassword(members); //업데이트


        //이메일 전송
        String email = members.getEmail();
        mailService.emailNewPwdSend(email,password);

        return ResponseEntity.ok(new ResponseDTO<Void>(null));
    }

    @Override
    public ResponseEntity<ResponseDTO<Void>> emailAuthCheck(EmailAuthCheckDTO emailAuthCheckDTO) {

        //엔티티 변환
        Members members = new Members(emailAuthCheckDTO);


        String email = members.getEmail();

        String authCode = members.getAuthCode();


        String redisKey = email+":authCd"; //이메일인증코드 키

        String authCodeResult = (String)inMemRepository.getValue(redisKey);

        String redisAttemptKey=email+":emlAuthAttemp"; //이메일 인증시도 횟수 키


        inMemRepository.increment(redisAttemptKey,1,5L,TimeUnit.MINUTES); //횟수 1 증가 , 5분 TTL

        //인증시도 횟수
        String attempt = (String)inMemRepository.getValue(redisAttemptKey);


        //5회 초과
        if(Integer.parseInt(attempt) >= MAX_ATTEMPT){

            throw new BadRequestException("ERR-EAUTH-CS-04");

        }

        if(authCodeResult==null){
            throw new BadRequestException("ERR-EAUTH-CS-02"); //인증코드 만료 혹은 보내지않음
        }

        boolean isAuth = bCryptPasswordEncoder.matches(authCode,authCodeResult);

        if(!isAuth){
            throw new BadRequestException("ERR-EAUTH-CS-03"); // 이메일 인증 코드 틀림  에러
        }

        String historySaveKey = email+":authHs"; //이메일 인증이력
        inMemRepository.save(historySaveKey,"Y",15L, TimeUnit.MINUTES);
        //인증코드 데이터 삭제
        inMemRepository.delete(redisKey);
        //인증 횟수 데이터 삭제
        inMemRepository.delete(redisAttemptKey);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO<Void>(null));

    }


}
