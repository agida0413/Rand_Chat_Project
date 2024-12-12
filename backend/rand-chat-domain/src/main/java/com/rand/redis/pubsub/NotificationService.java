package com.rand.redis.pubsub;

public interface NotificationService {
    public void registerConnection(String userId, String channel,String serverInstanceId,String key);
    public String getServerInstanceForUser(String userId, String channel,String key);
    public void removeConnection(String userId, String channel,String key);
}
