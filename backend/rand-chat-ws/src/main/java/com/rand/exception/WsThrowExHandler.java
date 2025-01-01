package com.rand.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.List;


//웹소켓 인터셉터에서 발생한 에러를 모아 컨트롤러에서 던져서 MessageExceptionHandler로 던지기 위함.
@Slf4j
public class WsThrowExHandler {

    public static void exThrowService(Message<?> incomingMessage){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(incomingMessage);
        List<String> errorList = (List<String>) incomingMessage.getHeaders().get("errorList");

        if(errorList!= null && errorList.size()>0 ){
            for (String error : errorList){
                throw new MessageDeliveryException(error);
            }
        }


    }


}
