package com.rand.chat.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ChatMessageData {
    private String chatRoomId;
    private String message;
    private String msgCrDateMs;
    private String msgCrDate;
    private boolean read;
    private ChatType chatType;
    private String nickName;


}
