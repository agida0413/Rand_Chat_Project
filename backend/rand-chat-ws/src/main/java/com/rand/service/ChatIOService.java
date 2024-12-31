package com.rand.service;

import com.rand.chat.dto.request.ReqChatMsgSaveDTO;
import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.common.ResponseDTO;
import com.rand.member.model.Members;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface ChatIOService {

    public void updateOfSend(Integer chatRoomId, String accessToken, ReqChatMsgSaveDTO reqChatMsgSaveDTO);
    public Boolean isRealYourChatRoom(RoomValidDTO roomValidDTO,String accessToken);
    public ResponseEntity<ResponseDTO<Void>> updateIsReadOfEnter(Integer chatRoomId, String accessToken);
}
