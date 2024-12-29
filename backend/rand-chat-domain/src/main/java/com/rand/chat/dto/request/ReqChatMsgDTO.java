package com.rand.chat.dto.request;

import com.rand.chat.model.ChatType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReqChatMsgDTO {

    private String message;
    private ChatType chatType;
    private LocalDateTime msgCrDate;
}
