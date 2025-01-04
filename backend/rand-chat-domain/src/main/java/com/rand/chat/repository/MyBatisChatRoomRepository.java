package com.rand.chat.repository;

import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.chat.dto.response.ResChatRoomListDTO;
import com.rand.chat.mapper.ChatRoomMapper;
import com.rand.chat.model.ChatRoom;
import com.rand.chat.model.ChatRoomForDelete;
import com.rand.chat.model.RoomValid;
import com.rand.member.model.Members;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MyBatisChatRoomRepository implements ChatRoomRepository {
    private final ChatRoomMapper chatMapper;
    @Override
    public RoomValid isRealYourRoom(RoomValidDTO roomValidDTO) {
        return chatMapper.isRealYourRoom(roomValidDTO);
    }

    @Override
    public List<ResChatRoomListDTO> selectChatRoomList(ChatRoom chatRoom){
        return chatMapper.selectChatRoomList(chatRoom);
    }
    @Override
    public List<Members> selectUsrIdInChatRoom(Integer chatRoomId){
        return chatMapper.selectUsrIdInChatRoom(chatRoomId);
    }

    @Override
    public ChatRoomForDelete selectChatRoomInfoForDelete(ChatRoomForDelete chatRoomForDelete) {
        return chatMapper.selectChatRoomInfoForDelete(chatRoomForDelete);
    }

    @Override
    public void chatRoomPycDel(ChatRoomForDelete chatRoomForDelete) {
        chatMapper.chatRoomPycDel(chatRoomForDelete);
    }

    @Override
    public void chatRoomLgcDel(ChatRoomForDelete chatRoomForDelete) {
        chatMapper.chatRoomLgcDel(chatRoomForDelete);
    }

    @Override
    public void chatRoomMemDel(ChatRoomForDelete chatRoomForDelete) {
        chatMapper.chatRoomMemDel(chatRoomForDelete);
    }
}
