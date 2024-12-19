package com.rand.redis.pubsub;

public interface PubSubRegistry {
    public void registerConnection(String userId, String channel,String key);
    public String getServerInstanceForUser(String userId, String channel,String key);
    public void removeConnection(String userId, String channel,String key);
}
