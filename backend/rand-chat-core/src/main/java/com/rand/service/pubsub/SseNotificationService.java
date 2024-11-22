package com.rand.service.pubsub;

import com.rand.common.Emmiter;
import com.rand.redis.InMemRepository;
import com.rand.redis.RedisRepositroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SseNotificationService implements NotificationService{

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
}
