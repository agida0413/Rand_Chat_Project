package com.rand.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rand.common.ErrorCode;
import com.rand.common.ResponseErr;
import com.rand.config.constant.PubSubChannel;
import com.rand.constant.ChatConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;


//Stomp 통신간 에러핸들링(연결 후 )
@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class WebSocketExceptionHandler {
    // 예외 발생 시 특정 사용자에게 에러 메시지 전송
    private final RedisTemplate<String,Object> redisTemplate;
    private final ObjectMapper objectMapper;
    
    @MessageExceptionHandler(MessagingException.class)
    public void handleException(MessagingException exception, Message<?> message) {
        log.info("핸들러 호출 ");
        // 메시지에서 세션 ID 추출
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 세션 Principal추출
        Principal principalObj = accessor.getUser();
        String principal = principalObj.getName();

        // 에러메시지 결정
        ErrorCode errorCode = null;
        String errCode = exception.getMessage();
        errorCode = selectErrorCode(errCode);
        
        //에러 객체생성
        ResponseErr responseErr = new ResponseErr(errorCode);
        //맵으로 변환
        Map<String,Object> map = objectMapper.convertValue(responseErr,Map.class);
        //구독서비스에서 핸들링할 맵 키 밸류 (발행주소,세션)
        map.put("pubUrl",ChatConst.PUB_CHAT_ERROR_URL);
        map.put("principal",principal);

        //채팅채널에 발행
        redisTemplate.convertAndSend(PubSubChannel.CHAT_CHANNEL.toString(),map);

    }
    //에러결정 메서드
    private ErrorCode selectErrorCode(String strErrorCode){
        ErrorCode selectedErrorCode = null;
        for(ErrorCode errorCode : ErrorCode.values()){
            if(errorCode.getCode().equals(strErrorCode)){
                selectedErrorCode=errorCode;
                break;
            }
        }
        if(selectedErrorCode==null){
            selectedErrorCode=ErrorCode.COMMON_UNPREDICTABLE_ERROR;
        }

        return  selectedErrorCode;
    }
}
