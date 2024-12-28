package com.rand.service;

import com.rand.chat.dto.response.ResChatRoomListDTO;
import com.rand.chat.model.ChatRoom;
import com.rand.chat.repository.ChatRoomRepository;
import com.rand.common.ResponseDTO;
import com.rand.custom.SecurityContextGet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService{
    private final ChatRoomRepository chatRoomRepository;
    @Override
    public ResponseEntity<ResponseDTO<List<ResChatRoomListDTO>>> selectChatRoomList(){
        String usrId =String.valueOf(SecurityContextGet.getUsrId());
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setUsrId(usrId);
        List<ResChatRoomListDTO> list = chatRoomRepository.selectChatRoomList(chatRoom);

        return ResponseEntity
                .ok()
                .body(new ResponseDTO<>(list));
    }
}
