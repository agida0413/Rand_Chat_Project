package com.rand.chat.repository;

import com.rand.chat.dto.RoomValidDTO;
import com.rand.chat.mapper.ChatMapper;
import com.rand.chat.model.RoomValid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MyBatisChatRepository implements ChatRepository{
    private final ChatMapper chatMapper;
    @Override
    public RoomValid isRealYourRoom(RoomValidDTO roomValidDTO) {
        return chatMapper.isRealYourRoom(roomValidDTO);
    }
}
