package com.rand.controller;

import com.rand.chat.dto.RoomValidDTO;
import com.rand.service.ChatApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/chat/api/v1/room")
public class ChatRoomApiController {
    private final ChatApiService chatApiService;

    @GetMapping
    public Boolean isRealYourRoom(@Valid  RoomValidDTO roomValidDTO){
       return chatApiService.isRealYourRoom(roomValidDTO);
    }

}
