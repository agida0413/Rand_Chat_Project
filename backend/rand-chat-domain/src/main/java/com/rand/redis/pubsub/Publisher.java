package com.rand.redis.pubsub;

import com.rand.config.constant.PubSubChannel;
import com.rand.config.constant.SSETYPE;
import com.rand.member.model.cons.MembersSex;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Publisher {
    private final StringRedisTemplate redisTemplate;

    //매칭결과
    public void sendNotification(String userId, String nickname,String profileImg,String sex,String type,String distance,String channel) {
        String payload = "";
        //매칭성공 시
        if(type.equals(SSETYPE.MATCHINGCOMPLETE.toString())){
            if(sex.equals(MembersSex.MAN.toString())){
                sex = "남자";
            }
            else{
                sex = "여자";
            }

        }
        // 매칭 성공 or 실패 시
        if(type.equals(SSETYPE.MATCHINGCOMPLETE.toString()) || type.equals(SSETYPE.MATCHINGTIMEOUT.toString())){
            payload = String.format("{\"userId\":\"%s\",\"nickname\":\"%s\",\"profileImg\":\"%s\",\"sex\":\"%s\",\"type\":\"%s\",\"distance\":\"%s\",\"channel\":\"%s\"}", userId, nickname,profileImg,sex,type,distance,channel);

            redisTemplate.convertAndSend(PubSubChannel.MATCHING_CHANNEL.toString(), payload);
        }


    }

    //매칭 수락
    public void SendMatchingAcceptNotify(String userId, String channel){
       String payload = String.format("{\"userId\":\"%s\",\"channel\":\"%s\"}", userId,channel);
        redisTemplate.convertAndSend(PubSubChannel.MATCHING_ACCEPT_CHANNEL.toString(), payload);
    }
}
