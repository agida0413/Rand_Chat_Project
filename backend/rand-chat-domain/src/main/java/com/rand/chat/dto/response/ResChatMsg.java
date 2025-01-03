package com.rand.chat.dto.response;

import com.rand.chat.model.ChatMessageData;
import com.rand.chat.model.ChatType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResChatMsg {
    private String chatRoomId;
    private String message;
    private String msgCrDateMs;
    private String msgCrDate;
    private boolean read;
    private ChatType chatType;
    private String nickName;

    public ResChatMsg(){

    }

}
