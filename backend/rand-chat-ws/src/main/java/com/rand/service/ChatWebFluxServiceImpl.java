package com.rand.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.common.ResponseDTO;
import com.rand.config.constant.PubSubChannel;
import com.rand.config.var.RedisKey;
import com.rand.constant.ChatConst;
import com.rand.custom.SecurityContextGet;
import com.rand.exception.custom.BadRequestException;
import com.rand.jwt.JWTUtil;
import com.rand.member.model.Members;
import com.rand.redis.InMemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatWebFluxServiceImpl implements ChatWebFluxService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final InMemRepository inMemRepository;
    private final JWTUtil jwtUtil;
    private final RedisTemplate<String,Object> redisTemplate;

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
    public void updateIsReadOfSend(Integer chatRoomId, String accessToken) {

        checkEnterUser(chatRoomId, accessToken)
                .doOnNext(member -> {
                    log.info("User ID: {}", member.getUsrId());
                    log.info("Nickname: {}", member.getNickName());

                    if (member.getNickName() != null) {
                        // 읽음 여부 업데이트 로직*******************

                        Integer usrId = jwtUtil.getUsrId(accessToken);
                        boolean isIamEnterRoom;
                        Integer roomId=(Integer) inMemRepository.getValue(RedisKey.CUR_ENTER_ROOM_KEY+String.valueOf(usrId));


                        if(roomId.equals(chatRoomId)){
                            isIamEnterRoom=true;
                        } else {
                            isIamEnterRoom = false;
                        }

                        if(isIamEnterRoom){
                            Map<String,Object> map = new HashMap<>();

                            map.put("chatRoomId",chatRoomId);
                            map.put("reader",member.getNickName());
                            map.put("readFlag",1);
                            map.put("pubUrl",ChatConst.PUB_CHAT_ROOM_URL);

                            redisTemplate.convertAndSend(PubSubChannel.CHAT_CHANNEL.toString(),map);
                        }
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

            if (enterRoomId != null && enterRoomId.equals(chatRoomId)) {
                return Mono.just(result); // 유효한 결과 반환
            } else {
                return Mono.empty(); // 결과가 없음을 나타냄
            }
        } catch (JsonProcessingException e) {
            return Mono.empty(); // 파싱 실패 시 빈 Mono 반환
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<Void>> enterRoomUpdateInfo(Integer chatRoomId , String access){
        // 비동기 WebClient 호출
        asyncEnterRoomUpdateInfo(chatRoomId, access)
                .doOnNext(result -> log.info("Enter Room Update Info result: {}", result))
                .filter(Boolean::booleanValue)  // Perform next step only when result is true
                .doOnNext(result -> {
                    // Perform the action you want when the result is true
                    log.info("Proceeding with further actions after successful room update.");
                    updateIsReadOfEnter(chatRoomId, access);
                })
                .onErrorResume(error -> {
                    // Handle error (optional)
                    log.error("Error in async enter room update: {}", error.getMessage());
                    return Mono.empty(); // Handle failure case
                })
                .subscribe(); // WebClient 결과를 비동기로

        return ResponseEntity
                .ok()
                .body(new ResponseDTO<Void>());
    }


    @Async
    private void updateIsReadOfEnter(Integer chatRoomId, String accessToken) {

        log.info("acc2={}",accessToken);

                        checkEnterUser(chatRoomId, accessToken)
                                .doOnNext(member -> {
                                    int secUsrId= jwtUtil.getUsrId(accessToken);

                                    // 읽음 여부 업데이트 로직*******************

                                    if (member.getNickName() != null) {

                                        Map<String,Object> map = new HashMap<>();

                                        map.put("chatRoomId",chatRoomId);
                                        map.put("reader",jwtUtil.getNickname(accessToken));
                                        map.put("readFlag",1);
                                        map.put("pubUrl",ChatConst.PUB_CHAT_ROOM_URL);


                                        redisTemplate.convertAndSend(PubSubChannel.CHAT_CHANNEL.toString(),map);

                                    }
                                })
                                .doOnError(error -> log.error("Error during updateIsRead: {}", error.getMessage()))
                                .subscribe(); // 비동기 실행


                }



    private Mono<Boolean> asyncEnterRoomUpdateInfo(Integer chatRoomId, String accessToken) {
        //실제 참여중인 채팅방인지 확인
        RoomValidDTO roomValidDTO = new RoomValidDTO();

        roomValidDTO.setUsrId(String.valueOf(jwtUtil.getUsrId(accessToken)));
        roomValidDTO.setChatRoomId(String.valueOf(chatRoomId));

        Boolean checkIsRealRoom = isRealYourChatRoom(roomValidDTO,accessToken);

        if(checkIsRealRoom ==null || checkIsRealRoom.equals(Boolean.FALSE)){
            throw new MessageDeliveryException("ERR-CHAT-API-03");
        }
        return webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/chat/api/v1/wx/room/enter/" + chatRoomId)
                        .build())
                .headers(headers -> headers.set("access", accessToken))
                .retrieve()
                .toBodilessEntity() // 응답 본문을 읽지 않고 상태만 확인
                .map(response -> response.getStatusCode().is2xxSuccessful()) // HTTP 상태가 2xx면 성공
                .onErrorResume(error -> {
                    log.error("WebClient error: {}", error.getMessage());
                    return Mono.just(false); // 오류 발생 시 실패 반환
                });
    }



}