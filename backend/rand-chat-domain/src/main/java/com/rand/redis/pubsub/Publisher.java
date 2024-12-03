package com.rand.redis.pubsub;

import com.rand.member.model.cons.MembersSex;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Publisher {
    private final StringRedisTemplate redisTemplate;

    public void sendNotification(String userId, String nickname,String profileImg,String sex,String type,String distance) {
        if(sex.equals(MembersSex.MAN.toString())){
            sex = "남자";
        }
        else{
            sex = "여자";
        }
        String payload = String.format("{\"userId\":\"%s\",\"nickname\":\"%s\",\"profileImg\":\"%s\",\"sex\":\"%s\",\"type\":\"%s\",\"distance\":\"%s\"}", userId, nickname,profileImg,sex,type,distance);
        redisTemplate.convertAndSend("notificationChannel", payload);
    }
}
