package com.rand.chat.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomForDelete {
private int usrId;
private int chatRoomId;
private RoomState roomState;
private int participantCnt;

public ChatRoomForDelete(int chatRoomId,int usrId){
    this.chatRoomId = chatRoomId;
    this.usrId = usrId;
}
public ChatRoomForDelete(){

}
}
