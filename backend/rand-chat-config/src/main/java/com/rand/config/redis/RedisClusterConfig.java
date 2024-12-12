package com.rand.config.redis;//package rand.api.web.config.redis;
//
//import io.lettuce.core.ReadFrom;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisClusterConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.util.Arrays;
//
//@Configuration
//public class RedisClusterConfig {
//
//    @Value("${spring.redis.cluster.nodes}")
//    private String clusterNodes;  // AWS Elasticache에서 제공하는 클러스터 엔드포인트
//
//    @Bean
//    public LettuceConnectionFactory redisClusterConnectionFactory() {
//        // 클러스터 노드들에 대한 엔드포인트를 콤마로 구분된 문자열로 받는다.
//        String[] nodes = clusterNodes.split(",");
//        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration(Arrays.asList(nodes));
//
//        // Lettuce 클라이언트 설정
//        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
//                .readFrom(ReadFrom.REPLICA_PREFERRED)  // 읽기 요청을 슬레이브에서 처리하도록 설정
//                .build();
//
//        return new LettuceConnectionFactory(clusterConfig, clientConfig);
//    }
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate() {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisClusterConnectionFactory());
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        return template;
//    }
//}
//
