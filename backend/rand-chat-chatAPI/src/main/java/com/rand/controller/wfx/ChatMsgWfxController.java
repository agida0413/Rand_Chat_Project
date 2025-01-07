package com.rand.controller.wfx;

import com.rand.chat.dto.request.ReqChatMsgSaveDTO;
import com.rand.chat.dto.request.ReqChatMsgUptDTO;
import com.rand.common.ResponseDTO;
import com.rand.service.wfx.ChatWfxApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//WebClient api
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/chat/api/v1/wx/chat")
public class ChatMsgWfxController {
    private final ChatWfxApiService chatWfxApiService;
    
    //WebFlux 채팅메시지 저장
    @PostMapping(consumes = "application/json")
    public ResponseEntity<ResponseDTO<Void>> asyncChatMsgSaveText(@RequestBody @Valid ReqChatMsgSaveDTO reqChatMsgSaveDTO){
        return chatWfxApiService.asyncSaveChatMsgText(reqChatMsgSaveDTO);
    }
    //WebFlux 채팅메시지 읽음여부 업데이트
    @PutMapping(consumes = "application/json")
    public ResponseEntity<ResponseDTO<Void>> asyncSaveChatMsgIsReadUpt(@RequestBody @Valid ReqChatMsgUptDTO reqChatMsgUptDTO){
        return chatWfxApiService.asyncSaveChatMsgIsReadUpt(reqChatMsgUptDTO);
    }
}
