//package it;
//
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.GenericContainer;
//import org.testcontainers.containers.MySQLContainer;
//import org.testcontainers.containers.wait.strategy.Wait;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.time.Duration;
//
//@TestConfiguration
//@Testcontainers
//class TestConfituration {
//
//    static final GenericContainer<?> redisContainer = new GenericContainer<>("redis:latest")
//            .withExposedPorts(6379);
//
//    static {
//        redisContainer.start();
//    }
//
//    @DynamicPropertySource
//    static void overrideProperties(DynamicPropertyRegistry registry) {
//        // Redis 설정
//        registry.add("spring.redis.host", redisContainer::getHost);
//        registry.add("spring.redis.port", () -> redisContainer.getMappedPort(6379));
//    }
//}
