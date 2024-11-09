package rand.api.domain.member.model;

import lombok.Getter;
import lombok.Setter;
import rand.api.web.dto.member.request.FindIdDTO;
@Getter @Setter
public class FindId {

    private String email;
    private String nickName;

    public FindId(FindIdDTO findIdDTO){
        this.email = findIdDTO.getEmail();
        this.nickName = findIdDTO.getNickName();
    }
}
