package com.rand.match.model;

import com.rand.match.dto.request.MatchAcceptDTO;
import com.rand.match.dto.request.MatchDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Match {
    //거리조건
    private double distance;
    //수락여부
    private boolean approveChk;
    
    //채팅방생성시
    private int roomMem1;
    private int roomMem2;
    //채팅방 상태
    private ChatRoomState roomState;

    //생성된 채팅방 번호
    private Long chatRoomId;

    public Match(MatchDTO matchDTO){
        this.distance = matchDTO.getDistance();
    }

    public Match (MatchAcceptDTO matchAcceptDTO){
        this.approveChk=matchAcceptDTO.isApproveChk();
    }
    public  Match(int roomMem1,int roomMem2){
        this.roomMem1=roomMem1;
        this.roomMem2=roomMem2;
    }
    public Match(){

    }

}
