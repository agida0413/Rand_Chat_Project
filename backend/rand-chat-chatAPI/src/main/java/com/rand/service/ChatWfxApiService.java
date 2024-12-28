package com.rand.service;

import com.rand.chat.dto.request.RoomValidDTO;

public interface ChatWfxApiService {
    public Boolean isRealYourRoom(RoomValidDTO roomValidDTO);
}
