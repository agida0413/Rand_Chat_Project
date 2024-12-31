package com.rand.service;



import org.springframework.data.redis.connection.Message;

import java.util.Map;

public interface ChatService {
    public void pubChatMessage(Message message);
    public void pubErrorMessage(Message message);
    public void pubIsRead(Message message);
}
