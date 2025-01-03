package com.rand.chat.mapper;

import com.rand.chat.model.ChatMessageSave;
import com.rand.chat.model.ChatMessageUpdate;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMsgMapper {
    public void chatMsgSave(ChatMessageSave chatMessageSave);
    public void chatMsgIsReadUpdate(ChatMessageUpdate chatMessageUpdate);
}
