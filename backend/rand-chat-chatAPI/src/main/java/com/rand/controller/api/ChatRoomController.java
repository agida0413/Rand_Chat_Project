package com.rand.controller.api;

import com.rand.chat.dto.response.ResChatMember;
import com.rand.chat.dto.response.ResChatRoomListDTO;
import com.rand.common.ResponseDTO;
import com.rand.common.service.PathVarValidationService;
import com.rand.exception.custom.BadRequestException;
import com.rand.service.api.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
//I/O API
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/api/v1/room")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    //채팅방 리스트 API
    @GetMapping
    public ResponseEntity<ResponseDTO<List<ResChatRoomListDTO>>> selectChatRoomList(){
        return chatRoomService.selectChatRoomList();
    }

    //채팅방 참여자 정보 리스트 API
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ResponseDTO<List<ResChatMember>>> selectMemberInfoInChatRoom(@PathVariable  Integer chatRoomId){
        boolean validation = PathVarValidationService.mustOverZero(chatRoomId);

        if(!validation){
            throw new BadRequestException("ERR-CHAT-API-VALI-01");
        }
        return chatRoomService.selectMemberInfoInChatRoom(chatRoomId);
    }



}
