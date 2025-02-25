package com.rand.chat.mapper;

import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.chat.dto.response.ResChatRoomListDTO;
import com.rand.chat.model.ChatRoom;
import com.rand.chat.model.ChatRoomForDelete;
import com.rand.chat.model.RoomValid;
import com.rand.member.model.Members;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatRoomMapper {
    public RoomValid isRealYourRoom(RoomValidDTO roomValidDTO);
    public List<ResChatRoomListDTO> selectChatRoomList(ChatRoom chatRoom);
    public List<Members> selectUsrIdInChatRoom(Integer chatRoomId);
    public ChatRoomForDelete selectChatRoomInfoForDelete(ChatRoomForDelete chatRoomForDelete);
    public void chatRoomPycDel(ChatRoomForDelete chatRoomForDelete);
    public void chatRoomLgcDel(ChatRoomForDelete chatRoomForDelete);
    public void chatRoomMemDel(ChatRoomForDelete chatRoomForDelete);
}
