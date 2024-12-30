package com.rand.controller;

import com.rand.common.ResponseDTO;
import com.rand.common.service.PathVarValidationService;
import com.rand.exception.custom.BadRequestException;
import com.rand.service.ChatWebFluxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/chat/ws/api")
public class SimpIoApiController {

    private final ChatWebFluxService chatWebFluxService;

    @GetMapping("/enter/{chatRoomId}")
    public ResponseEntity<ResponseDTO<Void>> enterRoomUpdateInfo(@PathVariable Integer chatRoomId, @RequestHeader("access") String accessToken){
        boolean validation = PathVarValidationService.mustOverZero(chatRoomId);

        if(!validation){
            throw new BadRequestException("ERR-CHAT-API-VALI-01");
        }
        return chatWebFluxService.enterRoomUpdateInfo(chatRoomId,accessToken);
    }
}
