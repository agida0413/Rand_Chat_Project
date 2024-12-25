package com.rand.service;

import com.rand.chat.dto.RoomValidDTO;

public interface ChatApiService {
    public Boolean isRealYourRoom(RoomValidDTO roomValidDTO);
}
