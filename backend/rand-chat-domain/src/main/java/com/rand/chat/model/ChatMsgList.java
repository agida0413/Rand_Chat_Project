package com.rand.chat.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMsgList {
    private int offset;
    private int rowSize;
    private int chatRoomId;

    public ChatMsgList(int offset,int rowSize, int chatRoomId){
        this.offset = offset;
        this.rowSize = rowSize;
        this.chatRoomId=  chatRoomId;
    }
}
