package com.rand.controller;

import com.rand.chat.dto.ChatDTO;
import com.rand.config.constant.PubSubChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatSocketController {

    private final RedisTemplate redisTemplate;
    @MessageMapping("/room/{roomId}")
            public void test(@Payload ChatDTO message,
                             @DestinationVariable int roomId,
                             Message<?> incomingMessage
                           ){
        concatMessage(incomingMessage,message,roomId);

        redisTemplate.convertAndSend(PubSubChannel.CHAT_CHANNEL.toString(),message);

            }


private Object concatMessage(Message<?> incomingMessage,Object object,int roomId){
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(incomingMessage);
    String usrId= (String)accessor.getSessionAttributes().get("usrId");
    if(object instanceof ChatDTO){
        ChatDTO chatDTO = (ChatDTO)object;
        chatDTO.setUsrId(usrId);
        chatDTO.setRoomId(roomId);

    }
        return object;
}
}
