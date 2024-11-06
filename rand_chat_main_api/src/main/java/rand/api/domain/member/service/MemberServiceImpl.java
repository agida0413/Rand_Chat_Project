package rand.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rand.api.domain.member.entity.EmailAuth;
import rand.api.domain.member.repository.MemberRepository;
import rand.api.domain.utilservice.MailService;
import rand.api.web.dto.common.ResponseDTO;
import rand.api.web.dto.member.request.EmailAuthDTO;
import rand.api.web.exception.custom.BadRequestException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final MailService mailService;
    @Override
    public ResponseEntity<ResponseDTO<Void>> emailDuplicateCheck(EmailAuthDTO emailAuthDTO) {
        //dto - > entity
        EmailAuth emailAuth = new EmailAuth(emailAuthDTO);

        //중복확인
        int presentCount = memberRepository.emailDuplicateCheck(emailAuth);

        if(presentCount !=0){
            // 이메일 중복 에러
            throw new BadRequestException("ERR-MEM-CUS-01");
        }



        //인증번호 전송
        //인증번호 레디스에 저장  인증번호 키값:이메일  밸류 = 인증번호
        mailService.emailSend(emailAuth);
        // 다시보낼시 덮어쓰기  TTL 3분


        //이메일이 존재하지 않을 시  회원가입 창 진입불가 (파라미터 + 레디스)

        return ResponseEntity.ok(new ResponseDTO<Void>(null));
    }
}
