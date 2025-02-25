package com.rand.util.mail;

public final class MailUtil {


    //인증코드 메일 html생성기
    public static String getCertificationMessage(String certificationNumber) {//인증코드를 매개변수로 받는다 .

        String certificationMessage="";//보낼 메시지
        //메일로보낼 html 결합
        certificationMessage+="<h1 style='text-align:center;'>[ Rand Chat! 회원가입] 인증메일 </h1>";
        certificationMessage+="<h3 style='text-align:center;'>인증코드:<strong style='font-size:32px; letter-spacing:8px;'> "+certificationNumber
                +"</strong></h3>";
        return certificationMessage;
    }

    public static String getCertificationUnlockMessage(String certificationNumber) {//인증코드를 매개변수로 받는다 .

        String certificationMessage="";//보낼 메시지
        //메일로보낼 html 결합
        certificationMessage+="<h1 style='text-align:center;'>[ Rand Chat! 계정활성화] 인증메일 </h1>";
        certificationMessage+="<h3 style='text-align:center;'>인증코드:<strong style='font-size:32px; letter-spacing:8px;'> "+certificationNumber
                +"</strong></h3>";
        return certificationMessage;
    }

    //임시 비밀번호 발송메일 html 생성기
    public static String getPasswordResetMessage(String newPassword) {//임시비밀번호를 받아온다 .

        //메일로보낼 html 결합
        String certificationMessage="";
        certificationMessage+="<h1 style='text-align:center;'>[ Rand Chat!임시비밀번호 발급 ] 메일 </h1>";
        certificationMessage+="<h3 style='text-align:center;'>새비밀번호:<strong style='font-size:32px; letter-spacing:8px;'> "+newPassword
                +"</strong></h3>";

        return certificationMessage;
    }


}
