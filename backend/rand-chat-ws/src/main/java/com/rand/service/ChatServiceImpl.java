package com.rand.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rand.constant.ChatConst;
import com.rand.jwt.JWTUtil;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.connection.Message;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService{
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final JWTUtil jwtUtil;

    //채팅전송 서비스
    @Override
    public void pubChatMessage(Message message) {
        String data= new String(message.getBody());
        
        try {
            Map<Object,Object> mapData = objectMapper.readValue(data,Map.class);

        String pubUrl = (String)mapData.get("pubUrl");
        String sessionToken=(String)mapData.get("usrId");
        Integer roomId=(Integer)mapData.get("roomId");
        log.info("하={}",sessionToken);
        String nickname = jwtUtil.getNickname(sessionToken);


            //발행주소 포맷팅 /pub/chat/room/{roomId}
             pubUrl += roomId;

             //닉네임 세팅
            mapData.put("nickname",nickname);


            //전송을 위한 map 가공
            mapData.remove("usrId");
            mapData.remove("pubUrl");



            data = objectMapper.writeValueAsString(mapData);
            //해당 발행주소로 메시지 발행(Stomp)
            simpMessagingTemplate.convertAndSend(pubUrl,data);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }
    //에러응답전송 서비스
    @Override
    public void pubErrorMessage(Message message) {
        String data= new String(message.getBody());

        try {
            Map<Object,Object> mapData = objectMapper.readValue(data,Map.class);
          //발행주소  
         String pubUrl = (String)mapData.get("pubUrl");
         //세션토큰
         String principal =(String) mapData.get("principal");

            //전송을 위한 map 가공
            mapData.remove("pubUrl");
            mapData.remove("principal");

            data = objectMapper.writeValueAsString(mapData);
            // 해당유저에게만 응답코드 전송
            simpMessagingTemplate.convertAndSendToUser(principal, pubUrl, data,createHeaders(principal));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
    //읽음업데이트
    @Override
    public void pubIsRead(Message message) {
        String data= new String(message.getBody());

        try {
            Map<Object,Object> mapData = objectMapper.readValue(data,Map.class);

            String pubUrl = (String)mapData.get("pubUrl") ;
            Integer roomId=(Integer)mapData.get("chatRoomId");
            String nickname =(String) mapData.get("reader");
            
            mapData.put("reader",nickname);
            mapData.put("type","READ-EVENT");

            //발행주소 포맷팅 /pub/chat/room/{roomId}
            pubUrl += roomId;

            mapData.remove("pubUrl");
            data = objectMapper.writeValueAsString(mapData);
            //해당 발행주소로 메시지 발행(Stomp)
            simpMessagingTemplate.convertAndSend(pubUrl,data);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private MessageHeaders createHeaders(@Nullable String sessionId){
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        if(sessionId!=null){
            headerAccessor.setSessionId(sessionId);
        }
        else{
            log.info("세션없음");
        }
        headerAccessor.setLeaveMutable(true);
        return  headerAccessor.getMessageHeaders();

    }
}
