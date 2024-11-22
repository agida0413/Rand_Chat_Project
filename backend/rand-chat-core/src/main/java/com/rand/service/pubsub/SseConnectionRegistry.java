package com.rand.service.pubsub;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentHashMap;

public class SseConnectionRegistry {
    // 클라이언트 ID (userId)와 SSE 연결을 관리하는 맵
    private static final ConcurrentHashMap<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    // 클라이언트의 SSE 연결을 등록
    public static void register(String userId, SseEmitter emitter) {
        sseEmitters.put(userId, emitter);
    }

    // 클라이언트의 SSE 연결을 조회
    public static SseEmitter getEmitter(String userId) {
        return sseEmitters.get(userId);
    }

    // 클라이언트의 SSE 연결을 제거
    public static void removeEmitter(String userId) {
        sseEmitters.remove(userId);
    }

    // 모든 SSE 연결을 가져옴
    public static ConcurrentHashMap<String, SseEmitter> getAllEmitters() {
        return sseEmitters;
    }
}
