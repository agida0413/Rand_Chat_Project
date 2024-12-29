package com.rand.controller;

import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.common.ResponseDTO;
import com.rand.member.model.Members;
import com.rand.service.ChatWfxApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/chat/api/v1/wx/room")
public class ChatRoomWfxAPIController {
    private final ChatWfxApiService chatApiService;

    @GetMapping
    public Boolean isRealYourRoom(@Valid  RoomValidDTO roomValidDTO){
       return chatApiService.isRealYourRoom(roomValidDTO);
    }

    @GetMapping("/{chatRoomId}/opsMem")
    public Members getOpsMem(@PathVariable Integer chatRoomId){
        return chatApiService.getOpsMem(chatRoomId);
    }

}
