package com.rand.service;

import reactor.core.publisher.Mono;

public interface ChatWebFluxService {

    public Boolean isRealYourChatRoom(String roomId);
}
