package rand.api.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;



import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class MemberRedisRepository implements MemberInMemRepository {

  private final RedisTemplate<String,Object> redisTemplate;

    public void emailAuthCodeSave(String Key , String certificationNumber,Long ttl){
        TimeUnit timeUnit = TimeUnit.MINUTES; // TTL을 분 단위로 설정

        redisTemplate.opsForValue().set(Key,certificationNumber,ttl,timeUnit);


    }


}
