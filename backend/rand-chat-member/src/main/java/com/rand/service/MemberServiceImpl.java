package com.rand.service;

import com.rand.common.ResponseDTO;
import com.rand.custom.SecurityContextGet;
import com.rand.exception.custom.BadRequestException;
import com.rand.member.dto.request.*;
import com.rand.member.dto.response.ResFindIdDTO;
import com.rand.member.model.Members;
import com.rand.member.model.cons.MembersState;
import com.rand.member.repository.MemberRepository;
import com.rand.redis.InMemRepository;
import com.rand.service.mail.MailService;
import com.rand.util.mail.RandomGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


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
    public ResponseEntity<ResponseDTO<Void>> emailUnlockAccount(UnlockAccountChkDTO unlockAccountChkDTO) {
        //엔티티 변환
        Members members = new Members(unlockAccountChkDTO);
        //실제 존재계정 여부 확인
        Members findMembers = memberRepository.findByEmail(members);

        if(findMembers ==null){
            throw new BadRequestException("ERR-EAUTH-CS-07"); //등록된 회원정보가 없습니다.
        }


        if(findMembers.getState().equals(MembersState.ACTIVE) || findMembers.getState().equals(MembersState.SUSPENDED)){
            throw new BadRequestException("ERR-EAUTH-CS-10");  // 계정상태가 이미 활성화거나 탈퇴조치된 계정임
        }

        String email = findMembers.getEmail();
        // 이메일 전송으로 저장된 인증코드 레디스 키
        String redisKey = email+":UnlockAccount"; //이메일인증코드 키
        //인증 코드 가져오기
        String redisCertificationNum = (String)inMemRepository.getValue(redisKey);

        //인증코드 자체가 저장되지않았거나 만료됌
        if(redisCertificationNum ==null){
            throw new BadRequestException("ERR-EAUTH-CS-02"); //인증코드 만료 혹은 보내지않음
        }

        //이메일 인증시도 횟수 키
        String redisAttemptKey=email+":unlockAttempt";

        //횟수 1 증가 , 5분 TTL
        inMemRepository.increment(redisAttemptKey,1,5L,TimeUnit.MINUTES);

        //인증시도 횟수
        String attempt = (String)inMemRepository.getValue(redisAttemptKey);


        //5회 초과
        if(Integer.parseInt(attempt) >= MAX_ATTEMPT){

            throw new BadRequestException("ERR-EAUTH-CS-04"); // 접근불가

        }

        // 일치여부
        String authCode = members.getAuthCode();
        boolean isAuth = bCryptPasswordEncoder.matches(authCode,redisCertificationNum);

        //틀릴 시
        if(!isAuth){
            throw new BadRequestException("ERR-EAUTH-CS-03"); // 이메일 인증 코드 틀림  에러
        }


        //성공 로직

        //인증코드 데이터 삭제
        inMemRepository.delete(redisKey);
        //인증 횟수 데이터 삭제
        inMemRepository.delete(redisAttemptKey);

        //계정 활성화 업데이트
        members.setState(MembersState.ACTIVE);

        memberRepository.activationMem(members);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO<Void>(null));
    }

    @Override
    public ResponseEntity<ResponseDTO<Void>> memberCurLocationUpdate(CurLocationDTO curLocationDTO) {

        Members members = new Members(curLocationDTO);
        members.setUsrId(SecurityContextGet.getUsrId());
        double lat = members.getLocaleLat();
        double lon = members.getLocaleLon();
        String usrId =String.valueOf(members.getUsrId());
        inMemRepository.saveLoc(usrId,lat,lon);

        return ResponseEntity.ok(new ResponseDTO<>(null));
    }

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
    public ResponseEntity<ResponseDTO<Void>> emailUnlockAccountSend(UnlockAccountDTO unlockAccountDTO) {
        Members members = new Members(unlockAccountDTO);

        Members findMembers = memberRepository.findByUsrAndEmail(members);
        // 이메일 + 아이디 정보가 없을 시

        if(findMembers==null){
            throw new BadRequestException("ERR-EAUTH-CS-07");

        }
        // 패스워드가 다를 시
        if(!bCryptPasswordEncoder.matches(members.getPassword(),findMembers.getPassword())){
            throw new BadRequestException("ERR-EAUTH-CS-09");
        }
        // 계정상태가 이미 활성화 상태 거나 삭제된 계정일 시
        if(findMembers.getState().equals(MembersState.ACTIVE) || findMembers.getState().equals(MembersState.SUSPENDED)){
            throw new BadRequestException("ERR-EAUTH-CS-10");
        }


        //이메일 인증코드 전송 및 레디스 인증코드 저장
        String email = findMembers.getEmail();

        mailService.emailUnlockAccountSend(email);

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
           throw new BadRequestException("ERR-EAUTH-CS-07");
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
