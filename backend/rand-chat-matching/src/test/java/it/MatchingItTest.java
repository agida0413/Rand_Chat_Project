package it;


import com.rand.member.model.Members;
import com.rand.member.model.cons.MembersSex;
import com.rand.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class MatchingItTest {

    @Autowired
    private MemberRepository memberRepository;
    @Test
    public void test(){
        Members members = new Members();

        members.setUsername("test123123");
                   members.setPassword("rla!104294242");
                   members.setEmail("agida0413123@naver.com");
                   members.setNickName("아아아아아아메리");
                   members.setSex(MembersSex.MAN);
                   members.setBirth(LocalDate.now());
                   members.setName("김용준");

        memberRepository.join(members);

        Members findMembers = memberRepository.findByEmail(members);

        Assertions.assertThat(findMembers).isNotNull();

    }
}
