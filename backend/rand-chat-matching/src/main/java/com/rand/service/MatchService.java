package com.rand.service;


import com.rand.common.ResponseDTO;
import com.rand.match.dto.MatchDTO;
import org.springframework.http.ResponseEntity;

public interface MatchService {

    public ResponseEntity<ResponseDTO<Void>> matchLogic(MatchDTO matchDTO);


}