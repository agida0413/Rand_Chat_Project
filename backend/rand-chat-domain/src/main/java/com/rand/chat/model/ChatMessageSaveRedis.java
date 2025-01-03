package com.rand.chat.model;

import com.rand.chat.dto.request.ReqChatMsgSaveDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ChatMessageSaveRedis {

    private String chatRoomId;
    private String message;
    private String msgCrDateMs;
    private String msgCrDate;
    private boolean read;
    private ChatType chatType;
    private String nickName;
    public ChatMessageSaveRedis(String nickName,ReqChatMsgSaveDTO reqChatMsgSaveDTO){
        this.chatRoomId =String.valueOf(reqChatMsgSaveDTO.getChatRoomId());
        this.message = reqChatMsgSaveDTO.getMessage();
        LocalDateTime msgCrDateMs=   reqChatMsgSaveDTO.getMsgCrDateMs();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String convertMsgCrDateMs = msgCrDateMs.format(formatter);
        this.msgCrDateMs = convertMsgCrDateMs;
        this.msgCrDate = reqChatMsgSaveDTO.getMsgCrDate();
        this.read = reqChatMsgSaveDTO.isRead();
        this.chatType = reqChatMsgSaveDTO.getChatType();
        this.nickName=nickName;
    }
}