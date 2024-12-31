package com.rand.chat.repository;

import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.chat.dto.response.ResChatRoomListDTO;
import com.rand.chat.model.ChatRoom;
import com.rand.chat.model.RoomValid;
import com.rand.member.model.Members;

import java.util.List;

public interface ChatRoomRepository {
    public RoomValid isRealYourRoom(RoomValidDTO roomValidDTO);
    public List<ResChatRoomListDTO> selectChatRoomList(ChatRoom chatRoom);
    public List<Members> selectUsrIdInChatRoom(Integer chatRoomId);

}
