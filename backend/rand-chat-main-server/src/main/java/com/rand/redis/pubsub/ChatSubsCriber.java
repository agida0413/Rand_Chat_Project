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
import com.rand.custom.SecurityContextGet;
import com.rand.match.dto.response.ResMatchAcceptDTO;
import com.rand.match.dto.response.ResMatchResultDTO;
import com.rand.match.model.AcceptState;
import com.rand.member.model.Members;
import com.rand.redis.InMemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
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
        String usrId;
        int roomId;

        try {

            Map<Object,Object> mapData = objectMapper.readValue(data,Map.class);
            usrId=(String)mapData.get("usrId");
            roomId=(Integer)mapData.get("roomId");



            mapData.remove("usrId");

            data = objectMapper.writeValueAsString(mapData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        String serverInstanceId = (String)inMemRepository.getValue(RedisKey.CHAT_SOCKET_KEY+usrId+":"+PubSubChannel.CHAT_CHANNEL);

        if(isCurrentInstance(serverInstanceId)){
         log.info("sendData={}",data);


            simpMessagingTemplate.convertAndSend("/sub/chat/room/"+roomId,data);
        }else{
            log.info("not send");
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


}
