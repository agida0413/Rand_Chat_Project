package com.rand.chat.repository;

import com.rand.chat.dto.response.ResChatMsg;
import com.rand.chat.model.ChatMessageSave;
import com.rand.chat.model.ChatMessageUpdate;
import com.rand.chat.model.ChatMsgList;

import java.util.List;

public interface ChatMsgRepository {
    public void chatMsgSave(ChatMessageSave chatMessageSave);
    public void chatMsgIsReadUpdate(ChatMessageUpdate chatMessageUpdate);
    public List<ResChatMsg> selectChatMsgList(ChatMsgList chatMsgList);

}
