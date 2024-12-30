package com.rand.service;

import com.rand.chat.dto.request.ReqChatMsgSaveDTO;
import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.chat.model.ChatMessageSave;
import com.rand.chat.model.RoomState;
import com.rand.chat.model.RoomValid;
import com.rand.chat.repository.ChatRoomRepository;
import com.rand.common.ResponseDTO;
import com.rand.common.service.CommonMemberService;
import com.rand.config.var.RedisKey;
import com.rand.custom.SecurityContextGet;
import com.rand.member.model.Members;
import com.rand.redis.InMemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatWfxApiServiceImpl implements ChatWfxApiService {
    private final ChatRoomRepository chatRepository;
    private final CommonMemberService commonMemberService;
    private final InMemRepository inMemRepository;
    //채팅방 메시지 저장
    @Override
    public ResponseEntity<Void> asyncSaveChatMsg(ReqChatMsgSaveDTO reqChatMsgSaveDTO) {
        String usrId = String.valueOf(SecurityContextGet.getUsrId());
        //현재채팅방의 내가 아닌 유저의 아이디번호

        //해당유저가 현재 채팅방에 참여중
        boolean isRead = false;

        ChatMessageSave chatMessageSave = new ChatMessageSave(reqChatMsgSaveDTO,usrId,isRead);


        return null;
    }

    //실제 채팅방에 참여중인지 검증하는 서비스
    @Override
    public Boolean isRealYourRoom(RoomValidDTO roomValidDTO) {
        RoomValid roomValid = chatRepository.isRealYourRoom(roomValidDTO);

        Boolean result =false;

        if(roomValid!=null && roomValid.getRoomState().equals(RoomState.ACTIVE.toString())){
            result = true;
        }

        return result;
    }
    @Override
    public Members getOpsMem(@PathVariable Integer chatRoomId) {
        List<Members> list = chatRepository.selectUsrIdInChatRoom(chatRoomId);
        int myUsrId = SecurityContextGet.getUsrId();

       Members memberInfo = new Members();
        for(Members members : list){
            if(members.getUsrId()== myUsrId){
                continue;
            }
            else{
                memberInfo =commonMemberService.memberGetInfoMethod(members.getUsrId());
            }
        }

        return memberInfo;
    }


    public ResponseEntity<ResponseDTO<Void>> asyncEnterRoomUpdateInfo(int usrId,Integer chatRoomId){
        inMemRepository.save(RedisKey.CUR_ENTER_ROOM_KEY+usrId,String.valueOf(chatRoomId));
        return ResponseEntity.ok().body(new ResponseDTO<>());
    }
}
