package com.rand.service;


import com.rand.common.ResponseDTO;
import com.rand.match.dto.request.MatchDTO;
import org.springframework.http.ResponseEntity;

public interface MatchService {

    public ResponseEntity<ResponseDTO<Void>> matchLogic(MatchDTO matchDTO);
    public void removeQueue1Min();
    public ResponseEntity<ResponseDTO<Void>> matchCancle();

}
