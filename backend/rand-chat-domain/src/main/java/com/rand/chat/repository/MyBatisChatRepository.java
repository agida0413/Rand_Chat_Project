package com.rand.chat.repository;

import com.rand.chat.dto.response.ResChatMsg;
import com.rand.chat.mapper.ChatMsgMapper;
import com.rand.chat.model.ChatMessageSave;
import com.rand.chat.model.ChatMessageUpdate;
import com.rand.chat.model.ChatMsgList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MyBatisChatRepository implements ChatMsgRepository{
    private final ChatMsgMapper chatMsgMapper;

    public void chatMsgSave(ChatMessageSave chatMessageSave){
        chatMsgMapper.chatMsgSave(chatMessageSave);
    }
    public void chatMsgIsReadUpdate(ChatMessageUpdate chatMessageUpdate){
        chatMsgMapper.chatMsgIsReadUpdate(chatMessageUpdate);
    }
    public List<ResChatMsg> selectChatMsgList(ChatMsgList chatMsgList){
        return chatMsgMapper.selectChatMsgList(chatMsgList);
    }
}
