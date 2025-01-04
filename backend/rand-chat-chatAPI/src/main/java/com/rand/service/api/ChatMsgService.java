package com.rand.service.api;

import com.rand.chat.dto.request.ReqImgSave;
import com.rand.chat.dto.response.ResChatMsg;
import com.rand.chat.dto.response.ResImgSave;
import com.rand.chat.model.ChatMessageData;
import com.rand.common.ResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ChatMsgService {

    public ResponseEntity<ResponseDTO<List<ResChatMsg>>> selectChatMsgListInMemory(Integer chatRoomId);
    public ResponseEntity<ResponseDTO<List<ResChatMsg>>> selectChatMsgListRDBMS(Integer chatRoomId,Integer page);
    public ResponseEntity<ResponseDTO<ResImgSave>> getSendImgUrl(ReqImgSave reqImgSave);
}
