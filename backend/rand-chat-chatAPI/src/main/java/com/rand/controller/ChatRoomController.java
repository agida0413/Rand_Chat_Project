package com.rand.controller;

import com.rand.chat.dto.response.ResChatRoomListDTO;
import com.rand.common.ResponseDTO;
import com.rand.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/api/v1/room")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    @GetMapping
    public ResponseEntity<ResponseDTO<List<ResChatRoomListDTO>>> selectChatRoomList(){
        return chatRoomService.selectChatRoomList();
    }

}
