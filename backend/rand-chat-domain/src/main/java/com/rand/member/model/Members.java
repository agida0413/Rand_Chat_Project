package com.rand.member.model;


import com.rand.match.dto.MatchDTO;
import com.rand.member.dto.request.*;
import com.rand.member.model.cons.MembersSex;
import com.rand.member.model.cons.MembersState;
import lombok.Getter;
import lombok.Setter;


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
private double localeLat;
private double localeLon;
private MembersSex sex;
private LocalDate birth;
private String name;

   //엔티티 컬럼 외 정보
    private String authCode; // 인증코드 ( 이메일 )
    private double distance;
    private String newPassword;

    //회원가입
    public Members(JoinDTO joinDTO) {
        this.username = joinDTO.getUsername();
        this.password = joinDTO.getPassword();
        this.email = joinDTO.getEmail();
        this.nickName = joinDTO.getNickName();
        this.sex = joinDTO.getSex();
        this.birth = joinDTO.getBirth();
        this.name = joinDTO.getName();
    }

    public Members() {
    }

    //아이디 찾기
    public Members(FindIdDTO findIdDTO) {
        
        this.email = findIdDTO.getEmail();
        this.name = findIdDTO.getName();
       
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
        this.name = resetPwdDTO.getName();
        this.username = resetPwdDTO.getUsername();
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

    public Members(CurLocationDTO curLocationDTO){
        this.localeLat = curLocationDTO.getLocaleLat();
        this.localeLon = curLocationDTO.getLocaleLon();

    }

    public Members(MatchDTO matchDTO){
        this.distance = matchDTO.getDistance();

    }
    public Members(UpdatePwdDTO updatePwdDTO){
        this.password = updatePwdDTO.getPassword();
        this.newPassword = updatePwdDTO.getNewPassword();
    }
    public Members(MemberDelDTO memberDelDTO){
        this.password = memberDelDTO.getPassword();
        this.state = MembersState.SUSPENDED;
    }
}
