package com.rand.config.redis;//package rand.api.web.config.redis;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class RedisTemplateConfig {
//
//    // 각 Redis 설정 값을 @Value로 주입
//    @Value("${spring.redis.cache.host}")
//    private String cacheHost;
//
//    @Value("${spring.redis.cache.port}")
//    private int cachePort;
//
//    @Value("${spring.redis.chat.host}")
//    private String chatHost;
//
//    @Value("${spring.redis.chat.port}")
//    private int chatPort;
//
//    @Value("${spring.redis.chat-replica.host}")
//    private String chatReplicaHost;
//
//    @Value("${spring.redis.chat-replica.port}")
//    private int chatReplicaPort;
//
//    @Value("${spring.redis.token.host}")
//    private String tokenHost;
//
//    @Value("${spring.redis.token.port}")
//    private int tokenPort;
//
//    @Value("${spring.redis.token-replica.host}")
//    private String tokenReplicaHost;
//
//    @Value("${spring.redis.token-replica.port}")
//    private int tokenReplicaPort;
//
//    @Value("${spring.redis.persistent.host}")
//    private String persistentHost;
//
//    @Value("${spring.redis.persistent.port}")
//    private int persistentPort;
//
//    @Value("${spring.redis.persistent-replica.host}")
//    private String persistentReplicaHost;
//
//    @Value("${spring.redis.persistent-replica.port}")
//    private int persistentReplicaPort;
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplateForCache() {
//        LettuceConnectionFactory factory = new LettuceConnectionFactory(cacheHost, cachePort);
//        factory.start();
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(factory);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        return template;
//    }
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplateForChat() {
//        LettuceConnectionFactory factory = new LettuceConnectionFactory(chatHost, chatPort);
//        factory.start();
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(factory);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        return template;
//    }
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplateForToken() {
//        LettuceConnectionFactory factory = new LettuceConnectionFactory(tokenHost, tokenPort);
//        factory.start();
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(factory);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        return template;
//    }
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplateForPersistent() {
//        LettuceConnectionFactory factory = new LettuceConnectionFactory(persistentHost, persistentPort);
//        factory.start();
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(factory);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        return template;
//    }
//
//    @Bean
//    public DynamicRedisTemplate dynamicRedisTemplate() {
//        Map<String, RedisTemplate<String, Object>> templates = new HashMap<>();
//        templates.put("redis-cache", redisTemplateForCache());
//        templates.put("redis-chat", redisTemplateForChat());
//        templates.put("redis-token", redisTemplateForToken());
//        templates.put("redis-persistent", redisTemplateForPersistent());
//
//        return new DynamicRedisTemplate(templates);
//    }
//}
