package com.rand.config.redis.pubsub;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
public class SseNotificationService implements NotificationService {

    private final StringRedisTemplate redisTemplate;

    public SseNotificationService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String SSE_CONNECTION_KEY = "sse:match:";

    // 특정 회원의 SSE 연결을 Redis에 저장
    public void registerConnection(String userId, String serverInstanceId) {
        redisTemplate.opsForValue().set(SSE_CONNECTION_KEY + userId, serverInstanceId);
    }

    // 특정 회원의 SSE 연결 정보 조회
    public String getServerInstanceForUser(String userId) {
        return redisTemplate.opsForValue().get(SSE_CONNECTION_KEY + userId);
    }

    // 연결 해제 시 삭제
    public void removeConnection(String userId) {
        redisTemplate.delete(SSE_CONNECTION_KEY + userId);
    }


    public SseEmitter connect(String userId) {
        SseEmitter emitter = new SseEmitter(180 * 1000L); // 90초 타임아웃
        SseConnectionRegistry.register(userId, emitter);
        registerConnection(userId, getCurrentServerInstanceId());
        emitter.onCompletion(() -> cleanup(userId));
        emitter.onTimeout(() -> cleanup(userId));
        emitter.onError(e -> cleanup(userId));
        return emitter;
    }

    private void cleanup(String userId) {
        SseConnectionRegistry.removeEmitter(userId);
        removeConnection(userId);
    }

    private String getCurrentServerInstanceId() {
        return System.getenv("INSTANCE_ID"); // 서버 인스턴스 ID 반환
    }
}
