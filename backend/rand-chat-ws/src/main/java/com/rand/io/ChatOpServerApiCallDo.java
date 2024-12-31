package com.rand.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rand.chat.dto.request.ReqChatMsgSaveDTO;
import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.common.ResponseDTO;
import com.rand.config.constant.PubSubChannel;
import com.rand.config.var.RedisKey;
import com.rand.constant.ChatConst;
import com.rand.exception.custom.BadRequestException;
import com.rand.jwt.JWTUtil;
import com.rand.member.model.Members;
import com.rand.redis.InMemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
// 실제 사용 서비스와 다른서버 api호출 로직 분리 - > IO DO 서비스
public class ChatOpServerApiCallDo implements ChatOpServerApiCall{
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final InMemRepository inMemRepository;
    private final JWTUtil jwtUtil;
    private final RedisTemplate<String,Object> redisTemplate;
    
    //채팅방에 참여하고 있는지 검증하는 Reactive WebClient API호출
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

    //해당 채팅방에 나 이외에 상대방이 참여하고있는지 확인하는 Reactive WebClient API호출
    @Override
    public Mono<Members> chkOpsMemIsEnter(Integer chatRoomId, String accessToken) {

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



   //본인이 채팅방에 입장함과 동시에 레디스Pub을 하는 Reactive Service
    public void updateIsReadOfEnterAndPub(Integer chatRoomId, String accessToken) {

        //상대방이 들어와 있을경우에만 pub할것이기 때문 ( pub의 목적은 프론트에서의 optimistic update)
        chkOpsMemIsEnter(chatRoomId, accessToken)
                .doOnNext(member -> {
                    if (member.getNickName() != null) {

                        Map<String,Object> map = new HashMap<>();

                        map.put("chatRoomId",chatRoomId);
                        map.put("reader",jwtUtil.getNickname(accessToken));
                        map.put("readFlag",1);
                        map.put("pubUrl", ChatConst.PUB_CHAT_ROOM_URL);

                        //pub
                        redisTemplate.convertAndSend(PubSubChannel.CHAT_CHANNEL.toString(),map);

                    }
                })
                .doOnError(error -> {
                    throw new MessageDeliveryException("ERR-WS-03");
                })
                .subscribe(); // 비동기 실행


    }

    //비동기로 채팅방 입장플래그를 레디스에 업데이트하는 API를 호출하는 Reactive 메소드
    public Mono<Boolean> asyncEnterRoomUpdateInfo(Integer chatRoomId, String accessToken) {

        //실제 참여중인 채팅방인지 확인
        RoomValidDTO roomValidDTO = new RoomValidDTO();

        roomValidDTO.setUsrId(String.valueOf(jwtUtil.getUsrId(accessToken)));
        roomValidDTO.setChatRoomId(String.valueOf(chatRoomId));

        Boolean checkIsRealRoom = isRealYourChatRoom(roomValidDTO,accessToken);

        if(checkIsRealRoom ==null || checkIsRealRoom.equals(Boolean.FALSE)){
            throw new BadRequestException("ERR-CHAT-API-03");
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
                    return Mono.just(false); // 오류 발생 시 실패 반환
                });
    }


    @Override
    public Mono<Boolean> asyncChatMsgSave(ReqChatMsgSaveDTO reqChatMsgSaveDTO,String accessToken) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/chat/api/v1/wx/chat")
                        .build())
                .headers(headers ->{
                    headers.set("access", accessToken);
                    headers.setContentType(MediaType.APPLICATION_JSON);
                } )
                .bodyValue(reqChatMsgSaveDTO)  // JSON 데이터를 요청 본문에 포함
                .retrieve()
                .toBodilessEntity()  // 응답 본문을 읽지 않고 상태만 확인
                .map(response -> response.getStatusCode().is2xxSuccessful())  // HTTP 상태가 2xx면 성공
                .onErrorResume(error -> {
                    return Mono.just(false);  // 오류 발생 시 실패 반환
                });
    }

    // 응답 파싱 및 Members 변환 로직
    private Mono<Members> parseResponse(String response, Integer chatRoomId) {
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


}
