package com.rand.chat.repository;

import com.rand.chat.mapper.ChatMsgMapper;
import com.rand.chat.model.ChatMessageSave;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MyBatisChatRepository implements ChatMsgRepository{
    private final ChatMsgMapper chatMsgMapper;
    public void chatMsgSave(ChatMessageSave chatMessageSave){
        chatMsgMapper.chatMsgSave(chatMessageSave);
    }
}
