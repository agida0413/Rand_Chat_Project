package unit;

import com.rand.MatchingApp;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MatchingApp.class)
public class MatchingUnitTest {


    @Test
    public void test(){
        int i = 1;
        Assertions.assertThat(i).isEqualTo(2);
    }
}
