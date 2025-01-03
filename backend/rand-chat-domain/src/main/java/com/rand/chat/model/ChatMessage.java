package com.rand.chat.model;

import com.rand.chat.dto.request.ReqChatMsgDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
public class ChatMessage {
    private String usrId;
    private String message;
    private int roomId;
    private String pubUrl;
    private ChatType chatType;
    private LocalDateTime msgCrDateMs;
    private String msgCrDate;
    private boolean read;

    public ChatMessage(ReqChatMsgDTO chatDTO) {
        this.message = chatDTO.getMessage();
        this.chatType = chatDTO.getChatType();
        this.msgCrDateMs = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Seoul"));
        this.msgCrDate = msgCrDateMs.truncatedTo(ChronoUnit.MINUTES) // 초와 나노초 제거
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")); // 형식 설정
        this.read =false;
    }


}
