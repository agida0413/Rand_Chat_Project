package com.rand.service;

import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.common.ResponseDTO;
import com.rand.member.model.Members;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface ChatIOService {

    public void updateIsReadOfSend(Integer chatRoomId,String accessToken);
    public Boolean isRealYourChatRoom(RoomValidDTO roomValidDTO,String accessToken);
    public ResponseEntity<ResponseDTO<Void>> updateIsReadOfEnter(Integer chatRoomId, String accessToken);
}
