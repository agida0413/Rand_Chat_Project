package com.rand.controller;

import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.common.ResponseDTO;
import com.rand.custom.SecurityContextGet;
import com.rand.member.model.Members;
import com.rand.service.ChatWfxApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

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
    @PutMapping("/enter/{chatRoomId}")
    public ResponseEntity<ResponseDTO<Void>> asyncEnterRoomUpdateInfo(@PathVariable Integer chatRoomId){
        return chatApiService.asyncEnterRoomUpdateInfo(
                SecurityContextGet.getUsrId(),chatRoomId);
    }


}
