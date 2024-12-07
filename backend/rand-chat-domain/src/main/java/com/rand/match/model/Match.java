package com.rand.match.model;

import com.rand.match.dto.request.MatchAcceptDTO;
import com.rand.match.dto.request.MatchDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Match {
    private double distance;
    private boolean approveChk;
    public Match(MatchDTO matchDTO){
        this.distance = matchDTO.getDistance();
    }

    public Match (MatchAcceptDTO matchAcceptDTO){
        this.approveChk=matchAcceptDTO.isApproveChk();
    }

}
