package com.rand.controller;

import com.rand.chat.dto.ChatDTO;
import com.rand.chat.model.ChatMessage;
import com.rand.common.ErrorCode;
import com.rand.config.constant.PubSubChannel;
import com.rand.constant.ChatConst;
import com.rand.util.ChatUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;

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
        ChatMessage chatMessage = new ChatMessage(message);
        ChatUtil.concatMessage(incomingMessage,chatMessage,roomId, ChatConst.PUB_CHAT_ROOM_URL);

       redisTemplate.convertAndSend(PubSubChannel.CHAT_CHANNEL.toString(),chatMessage);
    }



}
