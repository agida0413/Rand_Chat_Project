package com.rand.redis.pubsub;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Publisher {
    private final StringRedisTemplate redisTemplate;

    public void sendNotification(String userId, String nickname,String profileImg,String sex,String type,String distance) {
        String payload = String.format("{\"userId\":\"%s\",\"nickname\":\"%s\",\"profileImg\":\"%s\",\"sex\":\"%s\",\"type\":\"%s\",\"distance\":\"%s\"}", userId, nickname,profileImg,sex,type,distance);
        redisTemplate.convertAndSend("notificationChannel", payload);
    }
}
