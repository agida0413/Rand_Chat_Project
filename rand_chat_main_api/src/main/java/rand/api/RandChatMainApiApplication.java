package rand.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan
public class RandChatMainApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RandChatMainApiApplication.class, args);
	}

}
