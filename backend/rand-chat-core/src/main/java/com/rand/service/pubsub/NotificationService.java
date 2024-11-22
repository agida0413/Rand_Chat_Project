package com.rand.service.pubsub;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    public void registerConnection(String userId, String serverInstanceId);
    public String getServerInstanceForUser(String userId);
    public void removeConnection(String userId);
}
