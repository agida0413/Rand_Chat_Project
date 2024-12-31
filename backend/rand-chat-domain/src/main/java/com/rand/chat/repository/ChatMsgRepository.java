package com.rand.chat.repository;

import com.rand.chat.model.ChatMessageSave;

public interface ChatMsgRepository {
    public void chatMsgSave(ChatMessageSave chatMessageSave);
}
