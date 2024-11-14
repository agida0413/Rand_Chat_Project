package rand.chat.domain.member.model;

import lombok.Getter;
import lombok.Setter;
import rand.chat.domain.member.model.cons.MembersSex;
import rand.chat.domain.member.model.cons.MembersState;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
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

//엔티티 컬럼 외 정보
private String authCode; // 인증코드 ( 이메일 )


    public Members() {
    }


}
