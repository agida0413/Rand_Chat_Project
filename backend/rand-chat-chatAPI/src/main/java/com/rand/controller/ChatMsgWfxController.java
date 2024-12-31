package com.rand.controller;

import com.rand.chat.dto.request.ReqChatMsgSaveDTO;
import com.rand.common.ResponseDTO;
import com.rand.service.ChatWfxApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//WebClient api
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/chat/api/v1/wx/chat")
public class ChatMsgWfxController {
    private final ChatWfxApiService chatWfxApiService;
    @PostMapping(consumes = "application/json")
    public ResponseEntity<ResponseDTO<Void>> asyncChatMsgSaveText(@RequestBody ReqChatMsgSaveDTO reqChatMsgSaveDTO){

        return chatWfxApiService.asyncSaveChatMsgText(reqChatMsgSaveDTO);
    }
}
