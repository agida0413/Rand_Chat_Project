package rand.api.domain.member.model;

import lombok.Getter;
import lombok.Setter;
import rand.api.domain.member.model.cons.MembersSex;
import rand.api.domain.member.model.cons.MembersState;
import rand.api.web.dto.member.request.*;

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
    //회원가입
    public Members(JoinDTO joinDTO) {
        this.username = joinDTO.getUsername();
        this.password = joinDTO.getPassword();
        this.email = joinDTO.getEmail();
        this.nickName = joinDTO.getNickName();
        this.sex = joinDTO.getSex();
        this.birth = joinDTO.getBirth();
    }

    public Members() {
    }

    //아이디 찾기
    public Members(FindIdDTO findIdDTO) {
        
        this.email = findIdDTO.getEmail();
        this.nickName = findIdDTO.getNickName();
       
    }

    //아이디 찾기

    public Members(EmailAuthSendDTO emailAuthSendDTO) {

        this.email = emailAuthSendDTO.getEmail();

    }


    //이메일 인증 체크

    public Members(EmailAuthCheckDTO emailAuthCheckDTO) {

        this.email = emailAuthCheckDTO.getEmail();
        this.authCode=emailAuthCheckDTO.getAuthCode();
    }

    public Members(ResetPwdDTO resetPwdDTO){
        this.email = resetPwdDTO.getEmail();
        this.nickName = resetPwdDTO.getNickName();
    }

    public  Members(UnlockAccountDTO unlockAccountDTO){
        this.email = unlockAccountDTO.getEmail();
        this.username = unlockAccountDTO.getUsername();
        this.password = unlockAccountDTO.getPassword();
    }

    public  Members(UnlockAccountChkDTO unlockAccountChkDTO){
        this.email = unlockAccountChkDTO.getEmail();;
        this.authCode = unlockAccountChkDTO.getAuthCode();
    }
}
