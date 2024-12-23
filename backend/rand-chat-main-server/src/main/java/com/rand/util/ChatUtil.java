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



    public static Object concatMessage(Message<?> incomingMessage, Object object, int roomId, String pubUrl){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(incomingMessage);

        String usrId= (String)accessor.getSessionAttributes().get("usrId");

        if(object instanceof ChatMessage){
            ChatMessage chatMessage = (ChatMessage) object;
            chatMessage.setUsrId(usrId);
            chatMessage.setRoomId(roomId);
            chatMessage.setPubUrl(pubUrl);


        }
        return object;
    }



}
