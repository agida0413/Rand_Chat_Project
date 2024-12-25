package com.rand.service;

import com.rand.chat.dto.RoomValidDTO;
import reactor.core.publisher.Mono;

public interface ChatWebFluxService {

    public Boolean isRealYourChatRoom(RoomValidDTO roomValidDTO,String accessToken);
}
