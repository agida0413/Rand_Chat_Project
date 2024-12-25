package com.rand.chat.repository;

import com.rand.chat.dto.RoomValidDTO;
import com.rand.chat.model.RoomValid;

public interface ChatRepository {
    public RoomValid isRealYourRoom(RoomValidDTO roomValidDTO);
}
