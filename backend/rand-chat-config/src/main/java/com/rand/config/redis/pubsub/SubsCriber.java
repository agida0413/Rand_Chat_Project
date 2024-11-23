package com.rand.config.redis.pubsub;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Slf4j
@Component
public class SubsCriber implements MessageListener {

    private final StringRedisTemplate redisTemplate;
    private final NotificationService connectionService;

    public SubsCriber(StringRedisTemplate redisTemplate, NotificationService connectionService) {
        this.redisTemplate = redisTemplate;
        this.connectionService = connectionService;
    }

    // Redis 메시지 수신 처리
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String payload = new String(message.getBody());
        Map<String, String> data = null;
        try {
            data = parsePayload(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String userId = data.get("userId");
        String notificationMessage = data.get("message");

        // 현재 서버에 연결된 클라이언트라면 메시지 전송
        String serverInstanceId = connectionService.getServerInstanceForUser(userId);
        if (isCurrentInstance(serverInstanceId)) {
            sendToClient(userId, notificationMessage);
        }
    }

    private Map<String, String> parsePayload(String payload) throws JsonProcessingException {
        // JSON 문자열을 Map으로 변환 (간단한 파서 사용)
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(payload, new TypeReference<>() {});
    }

    private boolean isCurrentInstance(String serverInstanceId) {
        // 현재 서버 인스턴스와 비교 (로드밸런싱 환경에서 인스턴스 ID 비교)
        return getCurrentInstanceId().equals(serverInstanceId);
    }

    private void sendToClient(String userId, String message) {
        // SSE 연결된 클라이언트에게 메시지 전송
        SseEmitter emitter = SseConnectionRegistry.getEmitter(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(message));
                log.info("test result= {}",message);
                log.info("emmiter={}",emitter);
                log.info("emmiter str ={}",emitter.toString());
                log.info("emmiter id={}",userId);

            } catch (Exception e) {
                // 전송 실패 시 처리
                SseConnectionRegistry.removeEmitter(userId);
                log.info("test result= fail");
            }
        }
    }

    private String getCurrentInstanceId() {
        // 서버 인스턴스 ID 반환 (필요시 환경변수 또는 설정값 사용)
        log.info("serverId={}",System.getenv("INSTANCE_ID"));
        return System.getenv("INSTANCE_ID");
    }
}
