package com.rand.util;

import com.rand.chat.dto.ChatDTO;
import com.rand.chat.model.ChatMessage;
import com.rand.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;

@Component
public class ChatUtil {

    //MessageMapping에서 전달할 Message 객체를 변환해주는 Util
    public static Object concatMessage(Message<?> incomingMessage, Object object, int roomId, String pubUrl){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(incomingMessage);

        String usrId= accessor.getUser().getName();


        if(object instanceof ChatMessage){
            ChatMessage chatMessage = (ChatMessage) object;
            chatMessage.setUsrId(usrId);
            chatMessage.setRoomId(roomId);
            chatMessage.setPubUrl(pubUrl);

        }
        return object;
    }



}
