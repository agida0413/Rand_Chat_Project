package com.rand.service;

import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.member.model.Members;
import reactor.core.publisher.Mono;

public interface ChatWebFluxService {

    public Boolean isRealYourChatRoom(RoomValidDTO roomValidDTO,String accessToken);
    public Mono<Members> checkEnterUser(Integer chatRoomId, String accessToken);
    public void updateIsRead(Integer chatRoomId,String accessToken);
}
