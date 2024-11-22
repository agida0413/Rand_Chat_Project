package com.rand.service.pubsub;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Publisher {
    private final StringRedisTemplate redisTemplate;

    public void sendNotification(String userId, String message) {
        String payload = String.format("{\"userId\":\"%s\",\"message\":\"%s\"}", userId, message);
        redisTemplate.convertAndSend("notificationChannel", payload);
    }
}
