package com.rand.service.mail;

import com.rand.config.var.RedisKey;
import com.rand.exception.custom.InternerServerException;
import com.rand.member.model.Members;
import com.rand.member.repository.MemberRepository;
import com.rand.redis.InMemRepository;
import com.rand.util.mail.MailUtil;
import com.rand.util.mail.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class JavaMailService implements MailService {

    private final JavaMailSender javaMailSender;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final InMemRepository inMemRepository;

    @Override
    @Async
    public void emailUnlockAccountSend(String email) {
        try {

            MimeMessage message= javaMailSender.createMimeMessage(); //메일링 객체 생성
            MimeMessageHelper mimeMessageHelper= new MimeMessageHelper(message,true);

            String certificationNumber= RandomGenerator.generateRandomCode(); //랜덤 인증번호 생성



            String htmlContent= MailUtil.getCertificationUnlockMessage(certificationNumber); // 이메일로 보낼 html

            mimeMessageHelper.setTo(email);//보낼 상대
            mimeMessageHelper.setSubject("[Rand_Chat] 계정 활성화 인증코드입니다."); //제목
            mimeMessageHelper.setText(htmlContent,true);

            javaMailSender.send(message);//전송

            String redisKey = RedisKey.UNLOCK_ACCOUNT_KEY+email; //이메일인증코드 키

            certificationNumber=  bCryptPasswordEncoder.encode(certificationNumber);

            inMemRepository.save(redisKey,certificationNumber,3L, TimeUnit.MINUTES);


        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            //기타 에러
            throw new InternerServerException("ERR-EAUTH-CS-01");//사용자 정의 400에러 발생
        }
    }



    @Override
    @Async
    public void emailSend(Members members) {

        try {

            MimeMessage message= javaMailSender.createMimeMessage(); //메일링 객체 생성
            MimeMessageHelper mimeMessageHelper= new MimeMessageHelper(message,true);

            String certificationNumber= RandomGenerator.generateRandomCode(); //랜덤 인증번호 생성

            String email = members.getEmail();




            String htmlContent= MailUtil.getCertificationMessage(certificationNumber); // 이메일로 보낼 html

            mimeMessageHelper.setTo(email);//보낼 상대
            mimeMessageHelper.setSubject("[Rand_Chat] 회원가입 인증코드입니다."); //제목
            mimeMessageHelper.setText(htmlContent,true);

            javaMailSender.send(message);//전송
            String redisKey = RedisKey.JOIN_EMAIL_AUTH_CODE_KEY+email; //이메일인증코드 키

            certificationNumber=  bCryptPasswordEncoder.encode(certificationNumber);

            inMemRepository.save(redisKey,certificationNumber,3L, TimeUnit.MINUTES);


        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            //기타 에러
            throw new InternerServerException("ERR-EAUTH-CS-01");//사용자 정의 400에러 발생
        }




    }

    @Override
    @Async
    public void emailNewPwdSend(String email , String password) {
        try {

            MimeMessage message= javaMailSender.createMimeMessage(); //메일링 객체 생성
            MimeMessageHelper mimeMessageHelper= new MimeMessageHelper(message,true);



            String htmlContent=MailUtil.getPasswordResetMessage(password); // 이메일로 보낼 html

            mimeMessageHelper.setTo(email);//보낼 상대
            mimeMessageHelper.setSubject("[Rand_Chat] 임시패스워드 발급메일입니다."); //제목
            mimeMessageHelper.setText(htmlContent,true);
            javaMailSender.send(message);//전송



        } catch (Exception e) {
            // TODO: handle exception
            throw new InternerServerException("ERR-EAUTH-CS-01");//이메일 전송 실패
            // 익셉션 발생 = > 글로벌 핸들러에서잡음
        }

    }
}
