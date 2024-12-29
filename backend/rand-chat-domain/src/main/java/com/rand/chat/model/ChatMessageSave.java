package com.rand.chat.model;

import com.rand.chat.dto.request.ReqChatMsgSaveDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageSave {
private String usrId;
private String chatRoomId;
private String message;
private LocalDateTime msgCrDate;
private boolean isRead;
private ChatType chatType;

    public ChatMessageSave(ReqChatMsgSaveDTO reqChatMsgSaveDTO,String usrId,boolean isRead){
        this.usrId = usrId;
        this.chatRoomId =String.valueOf(reqChatMsgSaveDTO.getChatRoomId());
        this.message = reqChatMsgSaveDTO.getMessage();
        this.msgCrDate = reqChatMsgSaveDTO.getMsgCrDate();
        this.isRead = isRead;
        this.chatType = reqChatMsgSaveDTO.getChatType();
    }
}
