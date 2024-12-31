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
    @FutureOrPresent()
    private LocalDateTime msgCrDateMs;
    @NotNull
    @FutureOrPresent()
    private LocalDateTime msgCrDate;
    @Min(1)
    @NotNull
    @NotBlank
    private Integer chatRoomId;
    @NotNull
    private ChatType chatType;

    private boolean read;

    public ReqChatMsgSaveDTO(ChatMessage chatMessage){
        this.message = chatMessage.getMessage();
        this.msgCrDateMs =chatMessage.getMsgCrDateMs();
        this.msgCrDate = chatMessage.getMsgCrDate();
        this.chatRoomId = chatMessage.getRoomId();
        this.chatType = chatMessage.getChatType();
        this.read = false;
    }
    public ReqChatMsgSaveDTO(){

    }
}
