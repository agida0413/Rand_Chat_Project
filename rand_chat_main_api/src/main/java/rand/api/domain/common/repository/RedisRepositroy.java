package rand.api.domain.common.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisRepositroy  implements InMemRepository  {

  private final  RedisTemplate<String,Object> redisTemplate;
    private static final String GEO_KEY = "member:location";
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
    public void saveLoc(String usrId, double lat, double lon) {

        Point point = new Point(lon, lat);
        redisTemplate.opsForGeo().add(GEO_KEY,point,usrId);

    }

    @Override
    public Point getLoc(String usrId) {

        return redisTemplate.opsForGeo().position(GEO_KEY,usrId).stream().findFirst().orElse(null);
    }

    @Override
    public boolean scan(String key, String value) {
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();

        // 키를 매칭하는 ScanOptions 생성 (key 패턴을 지정)
        ScanOptions options = ScanOptions.scanOptions().match(key).build();

        Cursor<byte[]> cursor = connection.scan(options);

        try {
            while (cursor.hasNext()) {
                byte[] resultKey = cursor.next(); // 커서로부터 키를 가져옵니다.
                String resultKeyString = new String(resultKey); // byte[]를 String으로 변환

                // 키에 해당하는 값을 Redis에서 가져옵니다.
                String storedValue = (String)redisTemplate.opsForValue().get(resultKeyString);

                // 값이 null이 아니고 주어진 value와 일치하면 true 반환
                if (storedValue != null && storedValue.equals(value)) {
                    return true; // 해당 값이 존재하면 true
                }
            }
        } finally {
            cursor.close(); // Cursor 자원 해제
        }

        return false; // 주어진 값과 일치하는 키가 없다면 false 반환
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


    @Override
    public Object getHashValue(String key, String hashKey) {
        return  redisTemplate.opsForHash().get(key,hashKey);
    }

    @Override
    public Object getSetAllValue(String key) {
        return  redisTemplate.opsForSet().members(key);
    }



    @Override
    public void sortedSetSave(String key, String usrId, long timestamp) {
        redisTemplate.opsForZSet().add(key,usrId,timestamp);
    }

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
