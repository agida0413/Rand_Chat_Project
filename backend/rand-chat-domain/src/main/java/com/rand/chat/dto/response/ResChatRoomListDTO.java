package com.rand.chat.dto.response;

import com.rand.chat.model.ChatType;
import com.rand.chat.model.RoomState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResChatRoomListDTO {
    private String chatRoomId;
    private String opsProfileImg;
    private String opsNickName;
    private String curMsg;
    private String curMsgCrDate;
    private ChatType curChatType;
    private int unreadCount;
    private RoomState roomState;    // ACTIVE 활동 ,INACTIVE 상대방이 나감
    private boolean abNormalFlag;  // 비정상 플래그 true = 탈퇴한 회원 존재  ,false  : 정상 (계산은 닉네임이 널임 )
}
