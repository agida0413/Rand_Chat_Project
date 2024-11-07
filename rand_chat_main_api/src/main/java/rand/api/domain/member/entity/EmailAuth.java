package rand.api.domain.member.entity;

import lombok.Getter;
import lombok.Setter;
import rand.api.web.dto.member.request.EmailAuthDTO;

@Getter @Setter
public class EmailAuth {

private String email;


   public  EmailAuth (EmailAuthDTO emailAuthDTO){
        this.email = emailAuthDTO.getEmail();

    }
}
