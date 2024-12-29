package com.rand.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.config.var.RedisKey;
import com.rand.custom.SecurityContextGet;
import com.rand.exception.custom.BadRequestException;
import com.rand.member.model.Members;
import com.rand.redis.InMemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatWebFluxServiceImpl implements ChatWebFluxService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final InMemRepository inMemRepository;

    @Override
    public Boolean isRealYourChatRoom(RoomValidDTO roomValidDTO, String accessToken) {
        Boolean block = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/chat/api/v1/wx/room")
                        .queryParam("chatRoomId", roomValidDTO.getChatRoomId())  // chatRoomId를 쿼리 파라미터로 추가
                        .queryParam("usrId", roomValidDTO.getUsrId()) // usrId를 쿼리 파라미터로 추가
                        .build()) // URL 빌더를 사용하여 URI 구성
                .headers(headers -> {
                    headers.set("access", accessToken); // Authorization 헤더 추가
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

    @Override
    @Async
    public void updateIsRead(Integer chatRoomId, String accessToken) {
        checkEnterUser(chatRoomId, accessToken)
                .doOnNext(member -> {
                    log.info("User ID: {}", member.getUsrId());
                    log.info("Nickname: {}", member.getNickName());

                    if (member.getNickName() != null) {
                        // 읽음 여부 업데이트 로직
                        log.info("Updating read status for user: {}", member.getUsrId());
                        // pub 메시지 전송 로직 추가
                    }
                })
                .doOnError(error -> log.error("Error during updateIsRead: {}", error.getMessage()))
                .subscribe(); // 비동기 실행
    }

    @Override
    public Mono<Members> checkEnterUser(Integer chatRoomId, String accessToken) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/chat/api/v1/wx/room/" + chatRoomId + "/opsMem").build())
                .headers(headers -> headers.set("access", accessToken)) // Authorization 헤더 추가
                .retrieve()
                .bodyToMono(String.class) // 응답을 String으로 받음
                .onErrorResume(error -> {
                    log.error("Error occurred during WebClient call: {}", error.getMessage());
                    return Mono.just("{}"); // 기본 JSON 반환
                })
                .flatMap(response -> parseResponse(response, chatRoomId)); // 응답 파싱 및 로직 처리
    }

    // 응답 파싱 및 Members 변환 로직
    private Mono<Members> parseResponse(String response, Integer chatRoomId) {
        log.info("apiRes={}",response);
        try {
            Members result = objectMapper.readValue(response, Members.class);
            Integer enterRoomId = (Integer) inMemRepository.getValue(RedisKey.CUR_ENTER_ROOM_KEY + result.getUsrId());
                log.info("res = {}",result.getUsrId());
                log.info("enterRoomId = {}",enterRoomId);
            if (enterRoomId != null && enterRoomId.equals(chatRoomId)) {
                return Mono.just(result); // 유효한 결과 반환
            } else {
                log.warn("User is not in the specified chat room: {}", chatRoomId);
                return Mono.empty(); // 결과가 없음을 나타냄
            }
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON response: {}", e.getMessage());
            return Mono.empty(); // 파싱 실패 시 빈 Mono 반환
        }
    }
}