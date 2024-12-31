package com.rand.io;

import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.member.model.Members;
import reactor.core.publisher.Mono;

public interface ChatOpServerApiCall {
    public Boolean isRealYourChatRoom(RoomValidDTO roomValidDTO, String accessToken);
    public Mono<Members> chkOpsMemIsEnter(Integer chatRoomId, String accessToken);
    public void updateIsReadOfEnterAndPub(Integer chatRoomId, String accessToken);
    public Mono<Boolean> asyncEnterRoomUpdateInfo(Integer chatRoomId, String accessToken);
}
