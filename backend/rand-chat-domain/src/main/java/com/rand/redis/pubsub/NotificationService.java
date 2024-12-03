package com.rand.redis.pubsub;

public interface NotificationService {
    public void registerConnection(String userId, String serverInstanceId);
    public String getServerInstanceForUser(String userId);
    public void removeConnection(String userId);
}
