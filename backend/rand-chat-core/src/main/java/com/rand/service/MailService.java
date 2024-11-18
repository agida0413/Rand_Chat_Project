package com.rand.service;


import com.rand.member.model.Members;

public interface MailService {
    //이메일 인증코드 보내기 서비스 -- > 코드리턴
    public void emailSend(Members members);

   //이메일 임시패스워드 발급 서비스
    public void emailNewPwdSend(String email , String password);

   //비활성화 , 잠금 계정  잠금 풀기 이메일 전송
   public void emailUnlockAccountSend(String email);
}
