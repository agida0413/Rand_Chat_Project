package com.rand.controller.wfx;

import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.common.ResponseDTO;
import com.rand.custom.SecurityContextGet;
import com.rand.member.model.Members;
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
@RequestMapping("/chat/api/v1/wx/room")
public class ChatRoomWfxAPIController {
    private final ChatWfxApiService chatApiService;

    //실제 참여하고있는지 검증을 위한 api
    @GetMapping
    public Boolean isRealYourRoom(@Valid  RoomValidDTO roomValidDTO){
       return chatApiService.isRealYourRoom(roomValidDTO);
    }
    //상대방 정보를 조회하기위한 api
    @GetMapping("/{chatRoomId}/opsMem")
    public Members getOpsMem(@PathVariable Integer chatRoomId){
        return chatApiService.getOpsMem(chatRoomId);
    }
    //입장플래그를 업데이트하기 위한 API
    @PutMapping("/enter/{chatRoomId}")
    public ResponseEntity<ResponseDTO<Void>> asyncEnterRoomUpdateInfo(@PathVariable Integer chatRoomId){
        return chatApiService.asyncEnterRoomUpdateInfo(
                SecurityContextGet.getUsrId(),chatRoomId);
    }


}
