package com.rand.chat.mapper;

import com.rand.chat.dto.response.ResChatMsg;
import com.rand.chat.model.ChatMessageSave;
import com.rand.chat.model.ChatMessageUpdate;
import com.rand.chat.model.ChatMsgList;
import com.rand.chat.model.ImgSave;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMsgMapper {
    public void chatMsgSave(ChatMessageSave chatMessageSave);
    public void chatMsgIsReadUpdate(ChatMessageUpdate chatMessageUpdate);
    public List<ResChatMsg> selectChatMsgList(ChatMsgList chatMsgList);
    public void chatRoomImgSave(ImgSave imgSave);
}
