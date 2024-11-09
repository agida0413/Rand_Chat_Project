package rand.api.domain.member.entity;

import lombok.Getter;
import rand.api.web.dto.member.request.JoinDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class Members {

private int usrId;
private String username;
private String password;
private String email;
private String nickName;
private int pwdWrong;
private MembersState state;
private String profileImg;
private BigDecimal localeLat;
private BigDecimal localeLon;
private MembersSex sex;
private LocalDate birth;


    public Members(JoinDTO joinDTO) {
        this.username = joinDTO.getUsername();
        this.password = joinDTO.getPassword();
        this.email = joinDTO.getEmail();
        this.nickName = joinDTO.getNickName();
        this.sex = joinDTO.getSex();
        this.birth = joinDTO.getBirth();
    }
}
