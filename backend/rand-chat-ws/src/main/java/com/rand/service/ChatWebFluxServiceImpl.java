package com.rand.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatWebFluxServiceImpl implements ChatWebFluxService{

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Override
    public Boolean isRealYourChatRoom(String roomId) {
        Boolean block = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/chat/api/v1/valid/room")
                        .queryParam("roomId", roomId)  // roomId를 쿼리 파라미터로 추가
                        .build()) // URL 빌더를 사용하여 URI 구성
                .retrieve()
                .bodyToMono(String.class) // 응답을 String으로 받음
                .doOnError(error -> log.error("Error occurred: {}", error.getMessage())) // WebClient에서 발생한 예외를 처리
                .map(response -> {
                    try {
                        // JSON 응답을 Map으로 파싱
                        Map<String, Object> map = objectMapper.readValue(response, Map.class);
                        String status = (String) map.get("status");
                        log.info("status={}", status);
                        // 응답에서 "status"가 "200"인지 확인
                        return "200".equals(status); // status가 "200"이면 true 반환, 아니면 false
                    } catch (JsonProcessingException e) {
                        log.error("Error parsing response: {}", e.getMessage());
                        return false; // 파싱 오류 발생 시 false 반환
                    }
                })
                .onErrorReturn(false)  // 에러 발생 시 기본값 false 반환
                .block();
        return block;  // 비동기 처리를 동기적으로 대기
    }

}
