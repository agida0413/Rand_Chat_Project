package com.rand.io;

import com.rand.chat.dto.request.ReqChatMsgSaveDTO;
import com.rand.chat.dto.request.ReqChatMsgUptDTO;
import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.common.ResponseDTO;
import com.rand.member.model.Members;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface ChatOpServerApiCall {
    public Boolean isRealYourChatRoom(RoomValidDTO roomValidDTO, String accessToken);
    public Mono<Members> chkOpsMemIsEnter(Integer chatRoomId, String accessToken);
    public void updateIsReadOfEnterAndPub(Integer chatRoomId, String accessToken);
    public Mono<Boolean> asyncEnterRoomUpdateInfo(Integer chatRoomId, String accessToken);
    public Mono<Boolean> asyncChatMsgSaveTxt(ReqChatMsgSaveDTO reqChatMsgSaveDTO,String accessToken);
    public Mono<Boolean> asyncChatMsgIsReadUpt(ReqChatMsgUptDTO reqChatMsgUptDTO,String accessToken);
}
