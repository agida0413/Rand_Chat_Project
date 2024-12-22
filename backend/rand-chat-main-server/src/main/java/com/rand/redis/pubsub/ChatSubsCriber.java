package com.rand.redis.pubsub;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rand.common.ErrorCode;
import com.rand.common.ResponseDTO;
import com.rand.common.ResponseErr;
import com.rand.common.service.CommonMemberService;
import com.rand.config.constant.PubSubChannel;
import com.rand.config.constant.SSETYPE;
import com.rand.config.var.RedisKey;
import com.rand.constant.ChatConst;
import com.rand.custom.SecurityContextGet;
import com.rand.match.dto.response.ResMatchAcceptDTO;
import com.rand.match.dto.response.ResMatchResultDTO;
import com.rand.match.model.AcceptState;
import com.rand.member.model.Members;
import com.rand.redis.InMemRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatSubsCriber implements MessageListener {

 private final InMemRepository inMemRepository;
 private final SimpMessagingTemplate simpMessagingTemplate;
 private final ObjectMapper objectMapper;
 private final CommonMemberService commonMemberService;
    // Redis 메시지 수신 처리
    @Override
    public void onMessage(Message message, byte[] pattern) {

        String data= new String(message.getBody());

        String pubUrl;
        String usrId;
        int roomId;
        String principal="";

        try {

            Map<Object,Object> mapData = objectMapper.readValue(data,Map.class);

             pubUrl = (String)mapData.get("pubUrl");

             usrId=(String)mapData.get("usrId");
             log.info("puburl={}",pubUrl);

            if(pubUrl.equals(ChatConst.PUB_CHAT_ROOM_URL)){
                roomId=(Integer)mapData.get("roomId");
                pubUrl += roomId;
                log.info("convertPubR={}",pubUrl);


            }
            else if(pubUrl.equals(ChatConst.PUB_CHAT_ERROR_URL)){
                principal =(String) mapData.get("principal");


            }


            if(pubUrl.contains(ChatConst.PUB_CHAT_ROOM_URL)){
                mapData.remove("usrId");
                mapData.remove("pubUrl");

                data = objectMapper.writeValueAsString(mapData);
                simpMessagingTemplate.convertAndSend(pubUrl,data);
            }
            else if(pubUrl.equals(ChatConst.PUB_CHAT_ERROR_URL)){
                mapData.remove("sessionId");
                log.info("data={}",data);
                data = objectMapper.writeValueAsString(mapData);
                log.info("sessionIdInSub={}",principal);
                simpMessagingTemplate.convertAndSendToUser(principal, pubUrl, data,createHeaders(principal));
            }


        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }




    }


    private boolean isCurrentInstance(String serverInstanceId) {
        // 현재 서버 인스턴스와 비교 (로드밸런싱 환경에서 인스턴스 ID 비교)
        return getCurrentInstanceId().equals(serverInstanceId);
    }



    private String getCurrentInstanceId() {
        // 서버 인스턴스 ID 반환 (필요시 환경변수 또는 설정값 사용)
        log.info("serverId={}",System.getenv("INSTANCE_ID"));
        return System.getenv("INSTANCE_ID");
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
