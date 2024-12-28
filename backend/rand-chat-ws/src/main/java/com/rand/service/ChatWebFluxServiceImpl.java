package com.rand.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rand.chat.dto.request.RoomValidDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatWebFluxServiceImpl implements ChatWebFluxService{

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Override
    public Boolean isRealYourChatRoom(RoomValidDTO roomValidDTO,String accessToken) {
        Boolean block = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/chat/api/v1/wx/room")
                        .queryParam("chatRoomId", roomValidDTO.getChatRoomId())  // chatRoomId를 쿼리 파라미터로 추가
                        .queryParam("usrId", roomValidDTO.getUsrId()) // usrId를 쿼리 파라미터로 추가
                        .build()) // URL 빌더를 사용하여 URI 구성
                .headers(headers -> {
                    headers.set("access", accessToken); // Authorization 헤더 추가
                    headers.set("Custom-Header", "custom_value");      // Custom 헤더 추가
                })
                .retrieve()
                .bodyToMono(String.class) // 응답을 String으로 받음
                .doOnError(error -> log.error("Error occurred: {}", error.getMessage())) // WebClient에서 발생한 예외를 처리
                .map(response -> {
                    try {
                        Boolean result = objectMapper.readValue(response, Boolean.class); // Boolean으로 파싱
                        return result;
                    } catch (JsonProcessingException e) {
                        log.error("Error parsing response: {}", e.getMessage());
                        return false; // 파싱 오류 발생 시 false 반환
                    }
                })
                .onErrorReturn(false) // 에러 발생 시 기본값 false 반환
                .block();
        return block;  // 비동기 처리를 동기적으로 대기
    }

}
