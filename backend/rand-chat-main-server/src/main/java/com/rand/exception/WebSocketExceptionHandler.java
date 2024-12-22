package com.rand.exception;

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

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class WebSocketExceptionHandler {
    // 예외 발생 시 특정 사용자에게 에러 메시지 전송
    private final RedisTemplate<String,Object> redisTemplate;

    @MessageExceptionHandler(MessagingException.class)
    public void handleException(MessagingException exception, Message<?> message) {
        log.info("핸들러={}",exception);
        // 메시지에서 세션 ID 추출
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        String sessionId = (String) accessor.getSessionAttributes().get("usrId");
        Principal principalObj = accessor.getUser();
        String principal = principalObj.getName();
        log.info("sessionId={}",sessionId);
        // 클라이언트에게만 에러 메시지 전송
        ErrorCode errorCode = null;
        String errCode = exception.getMessage();
        errorCode = selectErrorCode(errCode);

        ResponseErr responseErr = new ResponseErr(errorCode);
        Map<String,Object> map = new HashMap<>();

        map.put("pubUrl",ChatConst.PUB_CHAT_ERROR_URL);
        map.put("code",responseErr.getCode());
        map.put("message",responseErr.getMessage());
        map.put("status",responseErr.getStatus());
        map.put("timeStamp",responseErr.getTimestamp());
        map.put("sessionId",sessionId);
        map.put("principal",principal);

        log.info("errres={}",responseErr);
        log.info("test={}",responseErr.getMessage());
        redisTemplate.convertAndSend(PubSubChannel.CHAT_CHANNEL.toString(),map);

    }

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
