package com.rand.redis.pubsub;

import com.rand.redis.InMemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatPubSubRegistry implements PubSubRegistry{

    private final InMemRepository inMemRepository;

    @Override
    public void registerConnection(String userId, String channel, String key) {
        inMemRepository.save(key + userId+":"+channel, getServerInstance());
    }

    @Override
    public String getServerInstanceForUser(String userId, String channel, String key) {
       return  "";
    }

    @Override
    public void removeConnection(String userId, String channel, String key) {
        inMemRepository.delete(key + userId+":"+channel);
    }

    private String getServerInstance(){
        return System.getenv("INSTANCE_ID"); // 서버 인스턴스 ID 반환

    }
}
