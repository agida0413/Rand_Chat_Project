package rand.api.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import rand.api.web.config.redis.DynamicRedisTemplate;


import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class MemberRedisRepository implements MemberInMemRepository {

   private final DynamicRedisTemplate dynamicRedisTemplate;

    public void emailAuthCodeSave(String redisKey , String certificationNumber,Long ttl){
        TimeUnit timeUnit = TimeUnit.MINUTES; // TTL을 분 단위로 설정
        String job ="cache";
        RedisTemplate<String,Object> redisTemplate= dynamicRedisTemplate.getTemplate(job);

        redisTemplate.opsForValue().set(redisKey,certificationNumber,ttl,timeUnit);


    }


}
