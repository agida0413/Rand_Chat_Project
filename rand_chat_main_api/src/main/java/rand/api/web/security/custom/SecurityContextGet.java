package rand.api.web.security.custom;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import rand.api.domain.member.model.cons.MembersState;
import rand.api.web.security.entity.CustomUserDetails;

import java.time.LocalDate;

public final class SecurityContextGet {

    //회원고유번호를 authentication에서 꺼내옴
    public static int getUsrId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();//securitycontext에서 authentication 가져옴

        CustomUserDetails userDetails= (CustomUserDetails)authentication.getPrincipal();//authentication에서 userdetails 가져옴
        int usrId =userDetails.getUsrId(); // userdetails (loadByUser 로부터 읽어온 getIdNum)

        return usrId;
    }

    // 로그인 필터에서는 성공시 리프레시 토큰 저장받을 때 idnum이 필요한데 , 그 시점에서는 로그인 필터 내부에 있는
    // authenrication 객체를 사용해야함 .
    //따라서 오버로딩 = > 회원고유번호를 authentication에서 꺼내옴
    public static int getUsrId(Authentication authentication) {

        CustomUserDetails userDetails= (CustomUserDetails)authentication.getPrincipal();
        int usrId =userDetails.getUsrId();
        return usrId;
    }


    public static String getNickname(Authentication authentication) {

        CustomUserDetails userDetails= (CustomUserDetails)authentication.getPrincipal();
        String nickname=userDetails.getNickname();
        return nickname;
    }

    public static String getNickname() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();//securitycontext에서 authentication 가져옴

        CustomUserDetails userDetails= (CustomUserDetails)authentication.getPrincipal();//authentication에서 userdetails 가져옴
        String nickname=userDetails.getNickname();
        return nickname;
    }

    public static String getUsername(Authentication authentication) {

        CustomUserDetails userDetails= (CustomUserDetails)authentication.getPrincipal();
        String username=userDetails.getUsername();
        return username;
    }

    public static String getUsername() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();//securitycontext에서 authentication 가져옴

        CustomUserDetails userDetails= (CustomUserDetails)authentication.getPrincipal();//authentication에서 userdetails 가져옴
        String username=userDetails.getUsername();
        return username;
    }

    public static String getSex(Authentication authentication) {

        CustomUserDetails userDetails= (CustomUserDetails)authentication.getPrincipal();
        String sex=userDetails.getSex().toString();
        return sex;
    }

    public static String getSex() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();//securitycontext에서 authentication 가져옴

        CustomUserDetails userDetails= (CustomUserDetails)authentication.getPrincipal();//authentication에서 userdetails 가져옴
        String sex=userDetails.getSex().toString();
        return sex;
    }

    public static LocalDate getBirth(Authentication authentication) {

        CustomUserDetails userDetails= (CustomUserDetails)authentication.getPrincipal();
        LocalDate birth=userDetails.getBirth();
        return birth;
    }

    public static LocalDate getBirth() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();//securitycontext에서 authentication 가져옴

        CustomUserDetails userDetails= (CustomUserDetails)authentication.getPrincipal();//authentication에서 userdetails 가져옴
        LocalDate birth=userDetails.getBirth();
        return birth;
    }


    // 계정 제어
    public static String getState(Authentication authentication) {

        CustomUserDetails userDetails= (CustomUserDetails)authentication.getPrincipal();
        String membersState =userDetails.getState().toString();
        return membersState;
    }

}
