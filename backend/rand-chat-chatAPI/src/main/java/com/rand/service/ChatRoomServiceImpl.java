package com.rand.service;

import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.chat.dto.response.ResChatMember;
import com.rand.chat.dto.response.ResChatRoomListDTO;
import com.rand.chat.model.ChatRoom;
import com.rand.chat.model.RoomValid;
import com.rand.chat.repository.ChatRoomRepository;
import com.rand.common.ResponseDTO;
import com.rand.common.service.CommonMemberService;
import com.rand.custom.SecurityContextGet;
import com.rand.exception.custom.BadRequestException;
import com.rand.member.dto.response.ResMemInfoDTO;
import com.rand.member.model.Members;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService{
    private final ChatRoomRepository chatRoomRepository;
    private final CommonMemberService commonMemberService;
    private final ChatWfxApiService chatWfxApiService;
    //회원의 참여중인 채팅방리스트를 조회하는 서비스
    @Override
    public ResponseEntity<ResponseDTO<List<ResChatRoomListDTO>>> selectChatRoomList(){

        String usrId =String.valueOf(SecurityContextGet.getUsrId());
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setUsrId(usrId);

        List<ResChatRoomListDTO> list = chatRoomRepository.selectChatRoomList(chatRoom);

        if(list.size()==0){

            throw new BadRequestException("ERR-CHAT-API-01");
        }

        return ResponseEntity
                .ok()
                .body(new ResponseDTO<>(list));
    }

    //현재 채팅방의 회원정보를 조회
    @Override
    public ResponseEntity<ResponseDTO<List<ResChatMember>>> selectMemberInfoInChatRoom(Integer chatRoomId){

        List<Members> usrList = chatRoomRepository.selectUsrIdInChatRoom(chatRoomId);

        //실제 참여중인 채팅방인지 확인
        RoomValidDTO roomValidDTO = new RoomValidDTO();
        int checkUsrId = SecurityContextGet.getUsrId();
        roomValidDTO.setUsrId(String.valueOf(checkUsrId));
        roomValidDTO.setChatRoomId(String.valueOf(chatRoomId));

        Boolean checkIsRealRoom = chatWfxApiService.isRealYourRoom(roomValidDTO);

        if(checkIsRealRoom ==null || checkIsRealRoom.equals(Boolean.FALSE)){
            throw new BadRequestException("ERR-CHAT-API-03");
        }

        if(usrList.size()==0){
         throw new BadRequestException("ERR-CHAT-API-02");
        }

        List<ResChatMember> membersList = new ArrayList<>();

        for(Members members : usrList){
           int usrId = members.getUsrId();
           Members resultMem = commonMemberService.memberGetInfoMethod(usrId);
            //1차변환
            ResMemInfoDTO resMemInfoDTO = new ResMemInfoDTO(resultMem);
            //2차변환
            boolean itsMe = false;

            if(resMemInfoDTO.getNickname().equals(SecurityContextGet.getNickname())){
                itsMe = true;
            }

            ResChatMember resChatMember = new ResChatMember(resMemInfoDTO,itsMe,0.0);
           membersList.add(resChatMember);
        }

    return ResponseEntity
        .ok()
        .body(new ResponseDTO<>(membersList));
    }

}
