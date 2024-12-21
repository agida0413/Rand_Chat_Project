package com.rand.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatDTO {

    private String usrId;
    private String message;
    private int roomId;
}
