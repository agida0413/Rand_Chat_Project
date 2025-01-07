package com.rand.util;

import com.rand.chat.model.ChatMessage;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatUtil {

    //MessageMapping에서 전달할 Message 객체를 변환해주는 Util
    public static Object concatMessage(Message<?> incomingMessage, Object object, int roomId, String pubUrl){

        String usrId= getStompHeaderAccessToken(incomingMessage);


        if(object instanceof ChatMessage){
            ChatMessage chatMessage = (ChatMessage) object;
            chatMessage.setUsrId(usrId);
            chatMessage.setRoomId(roomId);
            chatMessage.setPubUrl(pubUrl);
        }
        return object;
    }


    public static String getStompHeaderAccessToken(Message<?> message){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        //구독이나 메시지전송간 지속적으로 보내는 토큰
        List<String> accessTokenList = headerAccessor.getNativeHeader("access");
        String accessToken = (accessTokenList != null && !accessTokenList.isEmpty()) ? accessTokenList.get(0) : null;
        return accessToken;
    }



}
