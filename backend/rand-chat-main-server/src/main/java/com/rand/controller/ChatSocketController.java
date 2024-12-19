package com.rand.controller;

import com.rand.config.constant.PubSubChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatSocketController {

    private final RedisTemplate redisTemplate;
    @MessageMapping("/room/{roomId}")
            public void test(@Payload String message, @DestinationVariable String roomId){
            log.info("room={}",roomId);
                redisTemplate.convertAndSend(PubSubChannel.CHAT_CHANNEL.toString(),message);

            }



}
