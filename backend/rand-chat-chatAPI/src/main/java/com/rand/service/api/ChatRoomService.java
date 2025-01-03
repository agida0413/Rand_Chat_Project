package com.rand.service.api;

import com.rand.chat.dto.response.ResChatMember;
import com.rand.chat.dto.response.ResChatRoomListDTO;
import com.rand.common.ResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ChatRoomService {

    public ResponseEntity<ResponseDTO<List<ResChatRoomListDTO>>> selectChatRoomList();
    public ResponseEntity<ResponseDTO<List<ResChatMember>>> selectMemberInfoInChatRoom(Integer chatRoomId);
    public ResponseEntity<ResponseDTO<Void>> enterRoomDeleteInfo(Integer chatRoomId);
    public ResponseEntity<ResponseDTO<Void>> leaveChatRoom(Integer chatRoomId);

}
