package com.rand.chat.repository;

import com.rand.chat.model.ChatMessageSave;
import com.rand.chat.model.ChatMessageUpdate;

public interface ChatMsgRepository {
    public void chatMsgSave(ChatMessageSave chatMessageSave);
    public void chatMsgIsReadUpdate(ChatMessageUpdate chatMessageUpdate);

}
