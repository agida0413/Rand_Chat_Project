package com.rand.service;

import com.rand.chat.dto.response.ResChatRoomListDTO;
import com.rand.common.ResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ChatRoomService {

    public ResponseEntity<ResponseDTO<List<ResChatRoomListDTO>>> selectChatRoomList();
}
