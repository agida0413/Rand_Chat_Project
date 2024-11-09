package rand.api.domain.member.model;

import lombok.Getter;
import lombok.Setter;
import rand.api.web.dto.member.request.EmailAuthCheckDTO;

@Getter @Setter
public class EmailAuthCheck {
    private String email;
    private String authCode;

    public EmailAuthCheck(EmailAuthCheckDTO emailAuthCheckDTO) {
        this.email = emailAuthCheckDTO.getEmail();
        this.authCode = emailAuthCheckDTO.getAuthCode();
    }
}
