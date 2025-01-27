package com.rand.config.redis;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class NormalRedisConfig {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // Java 8 날짜/시간 처리
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);  // 타임스탬프 방지


        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        template.setValueSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();

        return template;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // Redis 서버 호스트와 포트를 설정
        return new LettuceConnectionFactory(host, port); // Docker 환경에서는 Redis 컨테이너 이름 사용
    }


//    @Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//        // ObjectMapper를 사용하여 Java 8 날짜/시간 타입 처리
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());  // Java 8 날짜/시간 처리
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);  // 타임스탬프 방지
//
//        // Jackson2JsonRedisSerializer를 사용하여 ResponseDTO 타입 직렬화
//        Jackson2JsonRedisSerializer<ResponseDTO> serializer = new Jackson2JsonRedisSerializer<>(ResponseDTO.class);
//
//        // Redis 캐시 설정 (TTL 60초)
//        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofSeconds(60))  // TTL 설정
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));  // Jackson2JsonRedisSerializer 적용
//
//        return RedisCacheManager.builder(connectionFactory)
//                .cacheDefaults(config)
//                .build();
//    }

}
