package com.rand.controller;

import com.rand.common.ResponseDTO;
import com.rand.common.service.PathVarValidationService;
import com.rand.exception.custom.BadRequestException;
import com.rand.service.ChatIOService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//채팅웹소켓 서버에서의 API
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/chat/ws/api")
public class SimpIoApiController {

    private final ChatIOService chatWebFluxService;

    //채팅방 입장시 수행해야할 API
    @GetMapping("/enter/{chatRoomId}")
    public ResponseEntity<ResponseDTO<Void>> enterRoomUpdateInfo(@PathVariable Integer chatRoomId, @RequestHeader("access") String accessToken){
    
        return chatWebFluxService.updateIsReadOfEnter(chatRoomId,accessToken);
    }
}
