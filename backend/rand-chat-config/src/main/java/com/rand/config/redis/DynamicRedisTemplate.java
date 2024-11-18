package com.rand.config.redis;//package rand.api.web.config.redis;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.core.RedisTemplate;
//
//import java.util.Map;
//
//public class DynamicRedisTemplate {
//    private final Map<String, RedisTemplate<String, Object>> templates;
//
//    public DynamicRedisTemplate(Map<String, RedisTemplate<String, Object>> templates) {
//        this.templates = templates;
//    }
//
//    public RedisTemplate<String, Object> getTemplate(String keyPrefix) {
//        if (keyPrefix.startsWith("cache")) {
//            return templates.get("redis-cache");
//        } else if (keyPrefix.startsWith("chat")) {
//            return templates.get("redis-chat");
//        }
//        else if (keyPrefix.startsWith("token")) {
//            return templates.get("redis-token");
//        }
//        else if (keyPrefix.startsWith("persistent")) {
//            return templates.get("redis-persistent");
//        }
//        else {
//            throw new IllegalArgumentException("Unknown key prefix: " + keyPrefix);
//        }
//
//
//    }
//}
