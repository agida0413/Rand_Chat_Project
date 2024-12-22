package com.rand.controller;

import com.rand.common.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat/api/v1")
public class ChatController {

@GetMapping("/token")
    public ResponseEntity<ResponseDTO<Void>> beforeWebSocketConnect(){
    return  new ResponseEntity<ResponseDTO<Void>>(null);
}
}
