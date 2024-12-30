package com.rand.controller;

import com.rand.chat.dto.request.ReqChatMsgDTO;
import com.rand.chat.dto.request.ReqChatMsgSaveDTO;
import com.rand.chat.model.ChatMessage;
import com.rand.common.service.PathVarValidationService;
import com.rand.config.constant.PubSubChannel;
import com.rand.constant.ChatConst;
import com.rand.service.ChatWebFluxService;
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

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatSocketController {

    private final RedisTemplate redisTemplate;
    private final ChatWebFluxService chatWebFluxService;
//채팅 stomp 통신
    @MessageMapping("/room/{roomId}")
            public void test(@Payload ReqChatMsgDTO message,
                             @DestinationVariable int roomId,
                             Message<?> incomingMessage
                           ){
        ChatMessage chatMessage = new ChatMessage(message);

       boolean validation =  PathVarValidationService.mustNotNull(chatMessage.getMessage());
       if(!validation){
           throw new MessageDeliveryException("ERR-WS-02");
       }
        ChatUtil.concatMessage(incomingMessage,chatMessage,roomId, ChatConst.PUB_CHAT_ROOM_URL);

       //메시지 전송시 상대방과 내가 해당방에 참여하는지 체크 후 읽음여부를 업데이트 및 웹소켓으로 읽음 플래그를 전송
        chatWebFluxService.updateIsReadOfSend(roomId,chatMessage.getUsrId());


        ReqChatMsgSaveDTO reqChatMsgSaveDTO  = new ReqChatMsgSaveDTO(chatMessage);
        //비동기 api(webClient) 호출 - > 영구저장
        
        //메시지 전송
       redisTemplate.convertAndSend(PubSubChannel.CHAT_CHANNEL.toString(),chatMessage);
    }



}
