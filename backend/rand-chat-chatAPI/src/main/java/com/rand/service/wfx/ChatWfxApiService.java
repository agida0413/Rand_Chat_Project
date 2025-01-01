package com.rand.service.wfx;

import com.rand.chat.dto.request.ReqChatMsgSaveDTO;
import com.rand.chat.dto.request.ReqChatMsgUptDTO;
import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.common.ResponseDTO;
import com.rand.member.model.Members;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface ChatWfxApiService {
    public Boolean isRealYourRoom(RoomValidDTO roomValidDTO);
    public Members getOpsMem(@PathVariable Integer chatRoomId);
    public ResponseEntity<ResponseDTO<Void>> asyncEnterRoomUpdateInfo(int usrId,Integer chatRoomId);
    public ResponseEntity<ResponseDTO<Void>> asyncSaveChatMsgText(ReqChatMsgSaveDTO reqChatMsgSaveDTO);
    public ResponseEntity<ResponseDTO<Void>> asyncSaveChatMsgIsReadUpt(ReqChatMsgUptDTO reqChatMsgUptDTO);
}
