package com.rand.service;

import com.rand.common.ResponseDTO;
import com.rand.match.dto.request.MatchAcceptDTO;
import com.rand.match.dto.response.ResMatchAcceptDTO;
import org.springframework.http.ResponseEntity;

public interface MatchAcceptService {
    public ResponseEntity<ResponseDTO<ResMatchAcceptDTO>> matchAccept(MatchAcceptDTO matchAcceptDTO, String matchToken);
}
