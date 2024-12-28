package com.rand.service;

import com.rand.chat.dto.response.ResChatMember;
import com.rand.chat.dto.response.ResChatRoomListDTO;
import com.rand.common.ResponseDTO;
import com.rand.member.model.Members;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ChatRoomService {

    public ResponseEntity<ResponseDTO<List<ResChatRoomListDTO>>> selectChatRoomList();
    public ResponseEntity<ResponseDTO<List<ResChatMember>>> selectMemberInfoInChatRoom(Integer chatRoomId);
    public ResponseEntity<ResponseDTO<Void>> enterRoomUpdateInfo(Integer chatRoomId);
}
