package com.rand.match.model;

import com.rand.match.dto.request.MatchDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Match {
    private double distance;

    public Match(MatchDTO matchDTO){
        this.distance = matchDTO.getDistance();
    }
}
