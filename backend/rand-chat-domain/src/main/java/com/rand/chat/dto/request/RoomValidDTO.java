package com.rand.chat.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomValidDTO {
    @NotNull
    private String usrId;
    @Min(1)
    private String chatRoomId;
}
