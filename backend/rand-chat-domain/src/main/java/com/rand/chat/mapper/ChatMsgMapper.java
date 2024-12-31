package com.rand.chat.mapper;

import com.rand.chat.model.ChatMessageSave;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMsgMapper {
    public void chatMsgSave(ChatMessageSave chatMessageSave);
}
