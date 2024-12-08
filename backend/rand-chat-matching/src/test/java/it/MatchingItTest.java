package it;


import com.rand.MatchingApp;
import com.rand.member.model.Members;
import com.rand.member.model.cons.MembersSex;
import com.rand.member.repository.MemberRepository;

import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MySQLContainer;

import java.time.LocalDate;

@SpringBootTest(classes = MatchingApp.class)
public class MatchingItTest {



//    @Autowired
//    private MemberRepository memberRepository;
//    @Test
//    public void test(){
//
//        Members members = new Members();
//
//        members.setUsername("agida0413");
//      members.setPassword("rla!1042922");
//                   members.setEmail("agida0413@naver.com");
//                   members.setNickName("아아아아아아메리");
//                   members.setSex(MembersSex.MAN);
//                   members.setBirth(LocalDate.now());
//                   members.setName("김용준");
//
//        memberRepository.join(members);
//
//        Members findMembers = memberRepository.findByEmail(members);
//
//
//        Assertions.assertThat(findMembers.getEmail()).isEqualTo(members.getEmail());
//
//
//    }
}
