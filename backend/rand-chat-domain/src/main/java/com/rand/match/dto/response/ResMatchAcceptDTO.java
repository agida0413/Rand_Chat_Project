package com.rand.match.dto.response;

import com.rand.match.model.AcceptState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResMatchAcceptDTO {
    //생성  채팅방 넘버
    private String roomId;
    private AcceptState acceptState;
    private String description;


    public void setDescription(){
        this.description = this.acceptState.getDescription();
    }
}
