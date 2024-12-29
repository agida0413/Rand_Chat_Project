package com.rand.chat.model;

import com.rand.chat.dto.request.ReqChatMsgDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
public class ChatMessage {
    private String usrId;
    private String message;
    private int roomId;
    private String pubUrl;
    private ChatType chatType;
    private LocalDateTime msgCrDate;

    public ChatMessage(ReqChatMsgDTO chatDTO) {
        this.message = chatDTO.getMessage();
        this.chatType = chatDTO.getChatType();
        this.msgCrDate = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Seoul"));
    }

    }
