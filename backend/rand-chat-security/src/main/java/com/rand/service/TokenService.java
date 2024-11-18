package com.rand.service;

import com.rand.common.ResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;


public interface TokenService {
    public void addRefresh(String key ,String token);
    public void deleteRefresh(String key,String token);
    public boolean isExist(String key,String token);
    public ResponseEntity<ResponseDTO<Void>> reissueToken(HttpServletRequest request, HttpServletResponse response);
}
