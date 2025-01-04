package com.rand.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rand.chat.dto.request.ReqChatMsgSaveDTO;
import com.rand.chat.dto.request.ReqChatMsgUptDTO;
import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.chat.model.ChatType;
import com.rand.common.ResponseDTO;
import com.rand.config.constant.PubSubChannel;
import com.rand.config.var.RedisKey;
import com.rand.constant.ChatConst;
import com.rand.exception.custom.BadRequestException;
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
    public void updateOfSend(Integer chatRoomId, String accessToken,ReqChatMsgSaveDTO reqChatMsgSaveDTO) {

            updateOfSendDo(chatRoomId,accessToken,reqChatMsgSaveDTO);

    }


    //본인이 입장했을 경우 비동기적으로 업데이트를 하는 서비스
    @Override
    public ResponseEntity<ResponseDTO<Void>> updateIsReadOfEnter(Integer chatRoomId , String access){
        // 비동기 WebClient 호출 - > 채팅방 업데이트
        chatOpServerApiCall.asyncEnterRoomUpdateInfo(chatRoomId, access)
                .doOnNext(result -> log.info("Enter Room Update Info result: {}", result))
                .filter(Boolean::booleanValue)  // Perform next step only when result is true
                .doOnNext(result -> {
                    //레디스Pub을 하는 Reactive Service  + 읽음여부 업데이트
                    chatOpServerApiCall.updateIsReadOfEnterAndPub(chatRoomId, access);
                })
                .onErrorResume(error -> {
                    throw new BadRequestException("ERR-CHAT-API-03");
                })
                .subscribe(); // WebClient 결과를 비동기로

        return ResponseEntity
                .ok()
                .body(new ResponseDTO<Void>());
    }

    //메시지 전송
    private void updateOfSendDo(Integer chatRoomId, String accessToken,ReqChatMsgSaveDTO reqChatMsgSaveDTO){
        //비동기 메시지 I/O 업데이트
        chatOpServerApiCall.asyncChatMsgSave(reqChatMsgSaveDTO,accessToken)
                .doOnNext(result -> log.info("updateOfSendResult: {}", result))
                .filter(Boolean::booleanValue)
                .doOnNext(result -> {
                    //성공적으로 메시지 저장햇을시 읽음 여부를 업데이트
                    //상대방이 입장했는지 확인하는 api
                    chatOpServerApiCall.chkOpsMemIsEnter(chatRoomId, accessToken)
                            .doOnNext(member -> {
                                //입장해있다면

                                if (member.getNickName() != null) {
                                    // 읽음 여부 업데이트 로직*******************
                                    int usrId = jwtUtil.getUsrId(accessToken);
                                    String nickName = jwtUtil.getNickname(accessToken);
                                    ReqChatMsgUptDTO reqChatMsgUptDTO = new ReqChatMsgUptDTO(usrId,nickName,chatRoomId);
                                    chatOpServerApiCall.asyncChatMsgIsReadUpt(reqChatMsgUptDTO, accessToken)
                                            .doOnNext(updateResult -> log.info("읽음 상태 업데이트 결과: {}", updateResult))
                                            .subscribe();

                                    //내가 입장해있는지 확인 (내가 입장해 있지않으면 , optimistic update를 할필요가 없으므로 이벤트 전송이 불필요하다.)
                                    boolean isIamEnterRoom;
                                    Integer roomId=(Integer) inMemRepository.getValue(RedisKey.CUR_ENTER_ROOM_KEY+String.valueOf(usrId));

                                    if(roomId !=null &&roomId.equals(chatRoomId)){
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
                })
                .onErrorResume(error -> {
                    throw new BadRequestException("");
                })
                .subscribe(); // WebClient 결과를 비동기로
    }



}