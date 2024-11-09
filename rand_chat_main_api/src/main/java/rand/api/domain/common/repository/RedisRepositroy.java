package rand.api.domain.common.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisRepositroy  implements InMemRepository  {

  private final  RedisTemplate<String,Object> redisTemplate;

    @Override
    public void save(String key, Object value, long ttl, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key,value,ttl,timeUnit);
    }

    @Override
    public void increment(String key, int incrementVal,long ttl,TimeUnit timeUnit) {
        redisTemplate.opsForValue().increment(key,incrementVal);

        redisTemplate.expire(key,ttl,timeUnit);
    }

    @Override
    public void hashSave(String key, String hashKey, Object value, long ttl, TimeUnit timeUnit) {
        redisTemplate.opsForHash().put(key,hashKey,value);

        redisTemplate.expire(key,ttl,timeUnit);
    }

    @Override
    public void listRightPush(String key, Object value, long ttl, TimeUnit timeUnit) {
        redisTemplate.opsForList().rightPush(key,value);

        redisTemplate.expire(key,ttl,timeUnit);
    }

    @Override
    public void setSave(String key, Object value, long ttl, TimeUnit timeUnit) {
        redisTemplate.opsForSet().add(key,value);

        redisTemplate.expire(key,ttl,timeUnit);
    }

    @Override
    public void save(String key, Object value) {
        redisTemplate.opsForValue().set(key,value);
    }

    @Override
    public void hashSave(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key,hashKey,value);
    }

    @Override
    public void listRightPush(String key, Object value) {
        redisTemplate.opsForList().rightPush(key,value);
    }

    @Override
    public void setSave(String key, Object value) {
        redisTemplate.opsForSet().add(key,value);
    }

    @Override
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

//    @Override
//    public T getListRange(String key, int start, int end) {
//        return redisTemplate.opsForList().range(key,start,end);
//    }

    @Override
    public Object getHashValue(String key, String hashKey) {
        return  redisTemplate.opsForHash().get(key,hashKey);
    }

    @Override
    public Object getSetAllValue(String key) {
        return  redisTemplate.opsForSet().members(key);
    }

//    @Override
//    public void listUpdate(String key, int index, T value) {
//        redisTemplate
//    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);

    }

    @Override
    public void hashDelete(String key, String hashKey) {
    redisTemplate.opsForHash().delete(key,hashKey);
    }

    @Override
    public void setDelete(String key, String value) {
    redisTemplate.opsForSet().remove(key,value);
    }
}
