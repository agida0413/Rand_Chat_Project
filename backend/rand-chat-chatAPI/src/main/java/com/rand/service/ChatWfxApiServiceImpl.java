package com.rand.service;

import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.chat.model.RoomState;
import com.rand.chat.model.RoomValid;
import com.rand.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatWfxApiServiceImpl implements ChatWfxApiService {
    private final ChatRoomRepository chatRepository;
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
}
