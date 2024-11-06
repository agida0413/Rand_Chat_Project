package rand.api.domain.utilservice;

import org.springframework.http.ResponseEntity;
import rand.api.domain.member.entity.EmailAuth;
import rand.api.domain.member.entity.Members;
import rand.api.web.dto.common.ResponseDTO;

public interface MailService {
    //이메일 인증코드 보내기 서비스 -- > 코드리턴
    public void emailSend(EmailAuth emailAuth);
    //이메일 인증코드 검증 서비스
    public boolean emailAuthValidation(EmailAuth emailAuth, String code);

//    //이메일 임시패스워드 발급 서비스
//    public ResponseEntity<ResponseDTO<Void>> emailPasswordReset(Members members);
}
