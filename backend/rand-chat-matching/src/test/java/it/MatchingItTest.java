package it;


import com.rand.MatchingApp;
import com.rand.member.model.Members;
import com.rand.member.model.cons.MembersSex;
import com.rand.member.repository.MemberRepository;

import com.rand.redis.InMemRepository;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

@SpringBootTest(classes = MatchingApp.class)
public class MatchingItTest {




    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private InMemRepository inMemRepository;
    @Test
    public void test(){

        Members members = new Members();

        members.setUsername("agida0413123");
      members.setPassword("rla!1042922");
                   members.setEmail("agida0413@naver.com123");
                   members.setNickName("아아아아아아메리");
                   members.setSex(MembersSex.MAN);
                   members.setBirth(LocalDate.now());
                   members.setName("김용준");

        memberRepository.join(members);

        Members findMembers = memberRepository.findByEmail(members);


        Assertions.assertThat(findMembers.getEmail()).isEqualTo(members.getEmail());




    }

    @Test
    public void test2(){
        inMemRepository.save("test","1");
        String t =(String) inMemRepository.getValue("test");

        Assertions.assertThat(t).isEqualTo("2");
    }
}
