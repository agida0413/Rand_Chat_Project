package rand.api.domain.member.entity;

import lombok.Getter;

@Getter
public class Members {
    private int usrId;
    private String username;
    private String password;
    private String email;
    private String name;
    private int pwdWrong;
    private MembersState state;
    private String profileImg;
    private String localeLat;
    private String localeLon;
}
