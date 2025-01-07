package com.rand.chat.model;

import com.rand.chat.dto.request.ReqChatMsgUptDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageUpdate {

    private int usrId; //읽음여부가 1 업데이트 될 유저아이디
    private Integer chatRoomId;
    private LocalDateTime readDate;

    public  ChatMessageUpdate(){

    }
    public ChatMessageUpdate(ReqChatMsgUptDTO reqChatMsgUptDTO){
        this.usrId = reqChatMsgUptDTO.getUsrId();
        this.chatRoomId = reqChatMsgUptDTO.getChatRoomId();
        this.readDate =reqChatMsgUptDTO.getReadDate();
    }
}
