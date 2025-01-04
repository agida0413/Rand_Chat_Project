package com.rand.controller.api;

import com.rand.chat.dto.request.ReqImgSave;
import com.rand.chat.dto.response.ResChatMsg;
import com.rand.chat.dto.response.ResImgSave;
import com.rand.common.ErrorCode;
import com.rand.common.ResponseDTO;
import com.rand.exception.custom.BadRequestException;
import com.rand.service.api.ChatMsgService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/api/v1/msg")
public class ChatMsgController {
    private final ChatMsgService chatMsgService;
    //채팅 메시지 정보 FROM REDIS 1페이지
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ResponseDTO<List<ResChatMsg>>> selectChatMsgListInMemory(@PathVariable Integer chatRoomId){
        //valication
        if(chatRoomId ==null ||chatRoomId== 0){
            throw new BadRequestException("ERR-CHAT-API-VALI-01");
        }
        return chatMsgService.selectChatMsgListInMemory(chatRoomId);
    }
    //채팅 메시지 정보 FROM RDBMS
    @GetMapping("/{chatRoomId}/{page}")
    public ResponseEntity<ResponseDTO<List<ResChatMsg>>> selectChatMsgListInMemory(@PathVariable Integer chatRoomId,@PathVariable Integer page){
        //valication
        if(chatRoomId ==null ||chatRoomId== 0){
            throw new BadRequestException("ERR-CHAT-API-VALI-01");
        }
        return chatMsgService.selectChatMsgListRDBMS(chatRoomId,page);
    }
    //사진 전송전 url get
    @PostMapping(value = "/img",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<ResImgSave>> getSendImgUrl(@ModelAttribute ReqImgSave reqImgSave){

        return chatMsgService.getSendImgUrl(reqImgSave);
    }
}
