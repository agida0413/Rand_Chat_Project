package com.rand.redis.pubsub;


import com.rand.common.ResponseDTO;
import com.rand.config.var.RedisKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@Slf4j
public class SseNotificationService implements NotificationService {

    private final StringRedisTemplate redisTemplate;

    public SseNotificationService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    // 특정 회원의 SSE 연결을 Redis에 저장
    public void registerConnection(String userId,String channel, String serverInstanceId,String key) {
        redisTemplate.opsForValue().set(key + userId+":"+channel, serverInstanceId);
    }

    // 특정 회원의 SSE 연결 정보 조회
    public String getServerInstanceForUser(String userId,String channel,String key) {
        return redisTemplate.opsForValue().get(key + userId+":"+channel);
    }

    // 연결 해제 시 삭제
    public void removeConnection(String userId,String channel,String key) {
        redisTemplate.delete(key + userId+":"+channel);
    }


    public SseEmitter connect(String userId,String channel,String key) {
        SseEmitter emitter = new SseEmitter(180 * 1000L); // 90초 타임아웃
        SseConnectionRegistry.register(userId,channel, emitter);
        registerConnection(userId, channel,getCurrentServerInstanceId(),key);
        emitter.onCompletion(() -> cleanup(userId,channel,key));
        emitter.onTimeout(() -> cleanup(userId,channel,key));
        emitter.onError(e -> cleanup(userId,channel,key));
        return emitter;
    }

    private void cleanup(String userId,String channel,String key) {
        SseConnectionRegistry.removeEmitter(userId,channel);
        removeConnection(userId,channel,key);
    }

    private String getCurrentServerInstanceId() {
        return System.getenv("INSTANCE_ID"); // 서버 인스턴스 ID 반환
    }
}
