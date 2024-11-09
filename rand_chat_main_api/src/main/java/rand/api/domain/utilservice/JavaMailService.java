package rand.api.domain.utilservice;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rand.api.domain.common.repository.InMemRepository;
import rand.api.domain.common.util.mail.MailUtil;
import rand.api.domain.common.util.mail.RandomGenerator;
import rand.api.domain.member.model.EmailAuthSend;
import rand.api.domain.member.repository.MemberRepository;
import rand.api.web.exception.custom.BadRequestException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import rand.api.web.exception.custom.InternerServerException;

import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class JavaMailService implements MailService{

    private final JavaMailSender javaMailSender;
    private final MemberRepository memberRepository;//멤버관련 레파지토리
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final InMemRepository inMemRepository;

    @Override
    @Async
    public void emailSend(EmailAuthSend emailAuth) {

        try {

            MimeMessage message= javaMailSender.createMimeMessage(); //메일링 객체 생성
            MimeMessageHelper mimeMessageHelper= new MimeMessageHelper(message,true);

            String certificationNumber= RandomGenerator.generateRandomCode(); //랜덤 인증번호 생성

            String email = emailAuth.getEmail();




            String htmlContent= MailUtil.getCertificationMessage(certificationNumber); // 이메일로 보낼 html

            mimeMessageHelper.setTo(email);//보낼 상대
            mimeMessageHelper.setSubject("[Rand_Chat] 회원가입 인증코드입니다."); //제목
            mimeMessageHelper.setText(htmlContent,true);

            javaMailSender.send(message);//전송
            String redisKey = email+":authCd"; //이메일인증코드 키
//            RedisConnection connection = ((RedisRoutingConfig.DynamicRedisConnectionFactory) redisTemplate.getConnectionFactory()).getConnection(redisKey);
//            // 이제 해당 연결로 Redis 작업 수행
//            connection.set(new StringRedisSerializer().serialize(redisKey), new GenericJackson2JsonRedisSerializer().serialize(certificationNumber));


            certificationNumber=  bCryptPasswordEncoder.encode(certificationNumber);

            inMemRepository.save(redisKey,certificationNumber,3L, TimeUnit.MINUTES);


        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            //기타 에러
            throw new InternerServerException("ERR-EAUTH-CS-01");//사용자 정의 400에러 발생
        }




    }


}
