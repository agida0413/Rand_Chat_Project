package com.rand.controller;

import com.rand.chat.dto.request.ReqChatMsgDTO;
import com.rand.chat.dto.request.ReqChatMsgSaveDTO;
import com.rand.chat.model.ChatMessage;
import com.rand.chat.model.ChatType;
import com.rand.common.service.PathVarValidationService;
import com.rand.config.constant.PubSubChannel;
import com.rand.constant.ChatConst;
import com.rand.exception.WsThrowExHandler;
import com.rand.service.ChatIOService;
import com.rand.util.ChatUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
//메시지 컨트롤러
@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatSocketController {

    private final RedisTemplate redisTemplate;
    private final ChatIOService chatIOService;

    //채팅 stomp 통신
    @MessageMapping("/room/{roomId}")
            public void test(@Payload ReqChatMsgDTO message,
                             @DestinationVariable int roomId,
                             Message<?> incomingMessage
                           ){
        //익셉션을 던지기위한 커스텀서비스
        WsThrowExHandler.exThrowService(incomingMessage);
        //채팅메시지 변환
        ChatMessage chatMessage = new ChatMessage(message);
        //유효성검증
       boolean validation =  PathVarValidationService.mustNotNull(chatMessage.getMessage());

       if(!validation){
           throw new MessageDeliveryException("ERR-WS-02");
       }

       //객체 변환
        ChatUtil.concatMessage(incomingMessage,chatMessage,roomId, ChatConst.PUB_CHAT_ROOM_URL);

        //메시지 전송시 상대방과 내가 해당방에 참여하는지 체크 후 읽음여부를 업데이트 및 웹소켓으로 읽음 플래그를 전송 + 메시지저장 I/O저장
        ReqChatMsgSaveDTO reqChatMsgSaveDTO  = new ReqChatMsgSaveDTO(chatMessage);
        String accessToken = ChatUtil.getStompHeaderAccessToken(incomingMessage);
        chatIOService.updateOfSend(roomId,accessToken,reqChatMsgSaveDTO);
        

        redisTemplate.convertAndSend(PubSubChannel.CHAT_CHANNEL.toString(),chatMessage);


    }



}
