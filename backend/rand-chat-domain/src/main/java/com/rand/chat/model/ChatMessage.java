package com.rand.chat.model;

import com.rand.chat.dto.ChatDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private String usrId;
    private String message;
    private int roomId;
    private String pubUrl;

    public ChatMessage(ChatDTO chatDTO){
        this.message=chatDTO.getMessage();
    }
}
