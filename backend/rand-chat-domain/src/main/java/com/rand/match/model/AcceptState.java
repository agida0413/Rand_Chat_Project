package com.rand.match.model;

import jdk.jfr.Description;

//매칭 수락 상태값
public enum AcceptState {

    WAITING("현재 클라이언트는 수락 , 상대방의 수락여부 대기중 "),  //상대방 수락여부 대기중
    SUCCESS("매칭성공 , 채팅방 생성완료") , // 매칭성공
    REFUSED("매칭실패 , 상대방이 거절함") , //상대방 거절
    CLOSE("현재 클라이언트가 거절, 매칭이 종료됌"); //내가 거절

    private final String description;

     AcceptState(String description){
    this.description = description;
    }

    public String getDescription(){
         return this.description;
    }
}
