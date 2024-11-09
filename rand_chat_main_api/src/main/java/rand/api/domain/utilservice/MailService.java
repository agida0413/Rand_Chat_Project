package rand.api.domain.utilservice;

import rand.api.domain.member.model.EmailAuthSend;

public interface MailService {
    //이메일 인증코드 보내기 서비스 -- > 코드리턴
    public void emailSend(EmailAuthSend emailAuth);

//    //이메일 임시패스워드 발급 서비스
//    public ResponseEntity<ResponseDTO<Void>> emailPasswordReset(Members members);
}
