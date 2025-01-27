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
private LocalDateTime msgCrDateMs;
private String msgCrDate;
private boolean read;
private ChatType chatType;
    public ChatMessageSave(String usrId,ReqChatMsgSaveDTO reqChatMsgSaveDTO){
        this.usrId = usrId;
        this.chatRoomId =String.valueOf(reqChatMsgSaveDTO.getChatRoomId());
        this.message = reqChatMsgSaveDTO.getMessage();
        this.msgCrDateMs = reqChatMsgSaveDTO.getMsgCrDateMs();
        this.msgCrDate = reqChatMsgSaveDTO.getMsgCrDate();
        this.read = reqChatMsgSaveDTO.isRead();
        this.chatType = reqChatMsgSaveDTO.getChatType();
    }
}
