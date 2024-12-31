package com.rand.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.common.ResponseDTO;
import com.rand.config.constant.PubSubChannel;
import com.rand.config.var.RedisKey;
import com.rand.constant.ChatConst;
import com.rand.io.ChatOpServerApiCall;
import com.rand.jwt.JWTUtil;
import com.rand.member.model.Members;
import com.rand.redis.InMemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
//웹소켓 서버(현재서버)에서 I/O 작업을 피하기위한 서비스 - > WebClient
public class ChatIOServiceImpl implements ChatIOService {

    private final InMemRepository inMemRepository;
    private final JWTUtil jwtUtil;
    private final RedisTemplate<String,Object> redisTemplate;
    private final ChatOpServerApiCall chatOpServerApiCall;
    
    //실제 채팅방에 참여중인지 검증을 위한 API호출(유일한 block)
    @Override
    public Boolean isRealYourChatRoom(RoomValidDTO roomValidDTO, String accessToken) {
         return chatOpServerApiCall.isRealYourChatRoom(roomValidDTO,accessToken);
    }

    @Override
    @Async
    //채팅메시지를 보낼 시 Reactive 및 비동기 업데이트 처리를 하는 서비스
    public void updateIsReadOfSend(Integer chatRoomId, String accessToken) {
        //상대방이 입장했는지 확인하는 api
       chatOpServerApiCall.chkOpsMemIsEnter(chatRoomId, accessToken)
                .doOnNext(member -> {
                    //입장해있다면
                    if (member.getNickName() != null) {
                        // 읽음 여부 업데이트 로직*******************

                        Integer usrId = jwtUtil.getUsrId(accessToken);

                        //내가 입장해있는지 확인 (ver 1 에서는 보낼시무조건 입장해있겠지만 , 추후 확장을 고려한다. [ 채팅방 미입장 빠른전송 ])
                        boolean isIamEnterRoom;
                        Integer roomId=(Integer) inMemRepository.getValue(RedisKey.CUR_ENTER_ROOM_KEY+String.valueOf(usrId));

                        if(roomId.equals(chatRoomId)){
                            isIamEnterRoom=true;
                        } else {
                            isIamEnterRoom = false;
                        }

                        //내가 입장해있다면
                        if(isIamEnterRoom){

                            Map<String,Object> map = new HashMap<>();
                            //읽음 플래그를 전송할 데이터생성
                            map.put("chatRoomId",chatRoomId);
                            map.put("reader",member.getNickName());
                            map.put("readFlag",1);
                            map.put("pubUrl",ChatConst.PUB_CHAT_ROOM_URL);
                            //pub
                            redisTemplate.convertAndSend(PubSubChannel.CHAT_CHANNEL.toString(),map);
                        }
                    }
                })
                .doOnError(error ->{
                    //메시지 전송 익셉션
                    throw new MessageDeliveryException("");
                })
                .subscribe(); // 비동기 실행
    }


    //본인이 입장했을 경우 비동기적으로 업데이트를 하는 서비스
    @Override
    public ResponseEntity<ResponseDTO<Void>> updateIsReadOfEnter(Integer chatRoomId , String access){
        // 비동기 WebClient 호출 - > 채팅방 업데이트
        chatOpServerApiCall.asyncEnterRoomUpdateInfo(chatRoomId, access)
                .doOnNext(result -> log.info("Enter Room Update Info result: {}", result))
                .filter(Boolean::booleanValue)  // Perform next step only when result is true
                .doOnNext(result -> {
                    //레디스Pub을 하는 Reactive Service
                   chatOpServerApiCall.updateIsReadOfEnterAndPub(chatRoomId, access);
                })
                .onErrorResume(error -> {
                   throw new MessageDeliveryException("ERR-WS-03");
                })
                .subscribe(); // WebClient 결과를 비동기로

        return ResponseEntity
                .ok()
                .body(new ResponseDTO<Void>());
    }



}