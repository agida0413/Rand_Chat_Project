package com.rand.service.api;

import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.chat.dto.response.ResChatMember;
import com.rand.chat.dto.response.ResChatRoomListDTO;
import com.rand.chat.model.ChatRoom;
import com.rand.chat.model.ChatRoomForDelete;
import com.rand.chat.model.RoomState;
import com.rand.chat.repository.ChatRoomRepository;
import com.rand.common.ErrorCode;
import com.rand.common.ResponseDTO;
import com.rand.common.service.CommonMemberService;
import com.rand.config.var.RedisKey;
import com.rand.custom.SecurityContextGet;
import com.rand.exception.custom.BadRequestException;
import com.rand.member.dto.response.ResMemInfoDTO;
import com.rand.member.model.Members;
import com.rand.redis.InMemRepository;
import com.rand.service.wfx.ChatWfxApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService{
    private final ChatRoomRepository chatRoomRepository;
    private final CommonMemberService commonMemberService;
    private final ChatWfxApiService chatWfxApiService;
    private final InMemRepository inMemRepository;
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

        double betweenDistance =0.0;
        if(usrList.size()>1){
            //두사용자 간 거리계산
        betweenDistance =   inMemRepository.calculateDistance(String.valueOf(usrList.get(0).getUsrId())
                    ,String.valueOf(usrList.get(1).getUsrId()));
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


            ResChatMember resChatMember = new ResChatMember(resMemInfoDTO,itsMe,betweenDistance);
           membersList.add(resChatMember);
        }

    return ResponseEntity
        .ok()
        .body(new ResponseDTO<>(membersList));
    }

    //채팅방 입장정보 초기화(로그아웃 시 수행)
    @Override
    public ResponseEntity<ResponseDTO<Void>> enterRoomDeleteInfo() {
        //사용자 고유번호
        int usrId = SecurityContextGet.getUsrId();
        //입장정보 레디스 키
        String key = RedisKey.CUR_ENTER_ROOM_KEY+usrId;

        inMemRepository.delete(key);

        return ResponseEntity
                .ok()
                .body(new ResponseDTO<>());
    }
    //채팅방 떠나기 (영구)
    @Override
    @Transactional
    public ResponseEntity<ResponseDTO<Void>> leaveChatRoom(Integer chatRoomId) {
        //회원 고유번호
        int usrId = SecurityContextGet.getUsrId();
        //데이터베이스 INPUT 엔티티 채팅방 아이디,회원 고유번호
        ChatRoomForDelete chatRoomForDelete = new ChatRoomForDelete(chatRoomId,usrId);
        //데이터베이스 OUTPUT 엔티티 채팅방 상태값 , 참여자 수
        ChatRoomForDelete chatRoomForDeleteResult = chatRoomRepository.selectChatRoomInfoForDelete(chatRoomForDelete);
        //채팅방이 존재하지 않음
        if(chatRoomForDeleteResult ==null || chatRoomForDeleteResult.getRoomState()==null){
            throw new BadRequestException("ERR-CHAT-API-05");
        }
        //채팅방 활성화 ACTIVE
        //내가 먼저 떠나는 경우(상대방은 입장해 있음)- 논리삭제
        if(chatRoomForDeleteResult.getRoomState().equals(RoomState.ACTIVE)){

            // 업데이트 전 ACTIVE 인데 상대방이 회원탈퇴로 인한 비정상 채팅방 인지 확인
            if(chatRoomForDeleteResult.getParticipantCnt()<2){
                //맞다면 물리삭제
                //채팅방 삭제 CASCADE 전부 삭제
                chatRoomRepository.chatRoomPycDel(chatRoomForDelete);

                //레디스 채팅정보 삭제
                inMemRepository.delete(RedisKey.CHAT_MESSAGE_LIST_KEY+chatRoomId);
            }else{
                //아니면 논리삭제
                //CHAT_ROOM_MEMBER 삭제
                chatRoomRepository.chatRoomMemDel(chatRoomForDelete);
                //CHAT_ROOM 채팅방 상태 업데이트
                chatRoomRepository.chatRoomLgcDel(chatRoomForDelete);
            }

        }
        else{
            //채팅방 활성화 INACTIVE
            //상대방이 이미 떠났고 , 나도 떠나는경우(물리삭제)
            //채팅방 삭제 CASCADE 전부 삭제
            chatRoomRepository.chatRoomPycDel(chatRoomForDelete);

            //레디스 채팅정보 삭제
            inMemRepository.delete(RedisKey.CHAT_MESSAGE_LIST_KEY+chatRoomId);

        }

        //현재 입장정보가 나가려는 채팅방일 시
        String curChatRoomId = (String) inMemRepository.getValue(RedisKey.CUR_ENTER_ROOM_KEY+usrId);

        if(curChatRoomId!= null && curChatRoomId.equals(String.valueOf(chatRoomId))){
            //입장 정보 삭제
            inMemRepository.delete(RedisKey.CUR_ENTER_ROOM_KEY+usrId);
        }



        return ResponseEntity
                .ok()
                .body(new ResponseDTO<>());
    }
}
