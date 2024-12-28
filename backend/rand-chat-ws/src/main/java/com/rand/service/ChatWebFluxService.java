package com.rand.service;

import com.rand.chat.dto.request.RoomValidDTO;

public interface ChatWebFluxService {

    public Boolean isRealYourChatRoom(RoomValidDTO roomValidDTO,String accessToken);
}
