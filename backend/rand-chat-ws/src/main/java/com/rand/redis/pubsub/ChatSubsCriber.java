package com.rand.redis.pubsub;


import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.rand.constant.ChatConst;
import com.rand.custom.SecurityContextGet;
import com.rand.jwt.JWTUtil;

import com.rand.service.ChatService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.stereotype.Component;


import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatSubsCriber implements MessageListener {


private final ChatService chatService;
private final ObjectMapper objectMapper;
    // Redis Pub/Sub 메시지 수신 처리
    @Override
    public void onMessage(Message message, byte[] pattern) {
        //데이터변환
        String data= new String(message.getBody());

        try {
            Map<Object,Object> mapData = objectMapper.readValue(data,Map.class);

            //발행주소
            String  pubUrl = (String)mapData.get("pubUrl");
            log.info(pubUrl);
             //만약 채팅방에대한 발행메시지일시
            if(pubUrl.equals(ChatConst.PUB_CHAT_ROOM_URL)){
                //읽기 알람을 위한 convertandsend

                Integer readFlag=(Integer)mapData.get("readFlag");

                if(readFlag!=null){

                    chatService.pubIsRead(message);
                }else{
                    chatService.pubChatMessage(message);
                }

            }
            //만약 에러전송을 위한 발행메시지일시
            else if(pubUrl.equals(ChatConst.PUB_CHAT_ERROR_URL)){
                chatService.pubErrorMessage(message);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }


}
