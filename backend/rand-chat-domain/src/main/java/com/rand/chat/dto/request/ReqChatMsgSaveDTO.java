package com.rand.chat.dto.request;

import com.rand.chat.model.ChatMessage;
import com.rand.chat.model.ChatType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class ReqChatMsgSaveDTO {
    @NotNull
    @NotBlank
    @Size(min = 1)
    private String message;
    @NotNull
    @FutureOrPresent(message = "Message creation date must be the current time or a future time.")
    private LocalDateTime msgCrDate;
    @Min(1)
    private String accessToken;

    @Min(1)
    @NotNull
    @NotBlank
    private Integer chatRoomId;
    @NotNull
    private ChatType chatType;

    public ReqChatMsgSaveDTO(ChatMessage chatMessage){
        this.message = chatMessage.getMessage();
        this.msgCrDate = chatMessage.getMsgCrDate();
        this.accessToken = chatMessage.getUsrId();
        this.chatRoomId = chatMessage.getRoomId();
        this.chatType = chatMessage.getChatType();
    }
}
