package it;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MatchingItTest {

    @Test
    public void test(){
        int i = 0;
        Assertions.assertThat(i).isEqualTo(2);
    }
}
