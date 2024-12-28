package com.rand.chat.mapper;

import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.chat.dto.response.ResChatRoomListDTO;
import com.rand.chat.model.ChatRoom;
import com.rand.chat.model.RoomValid;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatRoomMapper {
    public RoomValid isRealYourRoom(RoomValidDTO roomValidDTO);
    public List<ResChatRoomListDTO> selectChatRoomList(ChatRoom chatRoom);
}
