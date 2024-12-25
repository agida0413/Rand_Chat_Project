package com.rand.chat.mapper;

import com.rand.chat.dto.RoomValidDTO;
import com.rand.chat.model.RoomValid;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper {
    public RoomValid isRealYourRoom(RoomValidDTO roomValidDTO);
}
