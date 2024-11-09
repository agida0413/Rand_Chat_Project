package rand.api.domain.member.entity;

import lombok.Getter;
import lombok.Setter;
import rand.api.web.dto.member.request.EmailAuthSendDTO;

@Getter @Setter
public class EmailAuthSend {

private String email;


   public EmailAuthSend(EmailAuthSendDTO emailAuthDTO){
        this.email = emailAuthDTO.getEmail();

    }
}
