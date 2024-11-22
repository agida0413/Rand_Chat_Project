package com.rand.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    public void saveLoc(String usrId, double lat, double lon) {

        Point point = new Point(lon, lat);
        redisTemplate.opsForGeo().add(GEO_KEY,point,usrId);

    }

    @Override
    public Point getLoc(String usrId) {

        return redisTemplate.opsForGeo().position(GEO_KEY,usrId).stream().findFirst().orElse(null);
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
    public double calculateDistance(String usrId1, String usrId2) {


        Distance distance = redisTemplate.opsForGeo().distance(GEO_KEY ,usrId1 ,usrId2, RedisGeoCommands.DistanceUnit.METERS);

        // distance가 null인 경우 (두 사용자 간에 거리가 계산되지 않음)
        if (distance == null) {
            return -1; // -1로 반환하여 거리 계산이 실패했음을 나타낼 수 있습니다.
        }

        // 거리를 미터 단위로 반환
        return distance.getValue();
    }

    @Override
    public Set<String> geoRadius(String usrId, double radiusInMeters, int count) {
        // 반경 내 사용자의 위치를 조회
        Distance distance = new Distance(radiusInMeters, RedisGeoCommands.DistanceUnit.METERS);

        // 반경 조회에 대한 추가 옵션 설정 (좌표 및 거리 포함)
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs
                .newGeoRadiusArgs()
                .includeCoordinates()  // 좌표 포함
                .includeDistance()     // 거리 포함
                .limit(count);             // 가장 가까운 1명만 가져오기

        List<Point> points = redisTemplate.opsForGeo().position(GEO_KEY, usrId);

        Point point = points.get(0); // 사용자 좌표

        // Redis에서 주어진 member를 기준으로 반경 내 사용자들 조회
        GeoResults<RedisGeoCommands.GeoLocation<Object>> geoResults;
        geoResults = redisTemplate.opsForGeo().radius(GEO_KEY, new Circle(point, distance), args);

        // 결과가 없으면 빈 Set 반환
        if (geoResults == null || geoResults.getContent().isEmpty()) {
            return Collections.emptySet();
        }

        // 가장 가까운 사용자 한 명만 추출
        RedisGeoCommands.GeoLocation<Object> closestUser = geoResults.getContent().iterator().next().getContent();

        Set<String> nearestUserIds = new HashSet<>();
        nearestUserIds.add((String) closestUser.getName());

        return nearestUserIds;
    }

    @Override
    public Set<String> geoRadius(String usrId, double radiusInMeters) {

        Distance distance = new Distance(radiusInMeters, RedisGeoCommands.DistanceUnit.METERS);

        // 반경 조회에 대한 추가 옵션 설정
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs
                .newGeoRadiusArgs()
                .includeCoordinates()  // 좌표 포함
                .includeDistance();    // 거리 포함

        List<Point> points = redisTemplate.opsForGeo().position(GEO_KEY, usrId);

        Point point = points.get(0);


        // Redis에서 주어진 member를 기준으로 반경 내 사용자들 조회
        GeoResults<RedisGeoCommands.GeoLocation<Object>> geoResults;

        geoResults = redisTemplate.opsForGeo().radius(GEO_KEY, new Circle(point, distance), args);

        // 결과가 없으면 빈 Set 반환
        if (geoResults == null || geoResults.getContent().isEmpty()) {
            return Collections.emptySet();
        }

        // GeoResults에서 사용자 ID만 추출하여 Set으로 반환
        Set<String> nearbyUserIds = geoResults.getContent().stream()
                .map(geoResult -> (String) geoResult.getContent().getName())  // GeoLocation에서 사용자 ID 추출
                .collect(Collectors.toSet());

        return nearbyUserIds;
    }



    @Override
    public Object getSetAllValue(String key) {
        return  redisTemplate.opsForSet().members(key);
    }

    @Override
    public boolean lockSetting(String lockKey, String value, int expired) {
        TimeUnit timeUnit = TimeUnit.SECONDS;

        return redisTemplate.opsForValue().setIfAbsent(lockKey,value,expired,timeUnit);
    }

    @Override
    public Set<String> sortedSetRangeByScore(String key, long minScore, long maxScore) {

        Set<Object> result = redisTemplate.opsForZSet().rangeByScore(key, minScore, maxScore);

        // 결과가 null인 경우를 처리하기 위한 null 체크
        if (result == null) {
            return Collections.emptySet();
        }

        // Object 타입을 String으로 변환하여 Set<String> 반환
        return result.stream()
                .map(String::valueOf)  // Object를 String으로 변환
                .collect(Collectors.toSet());
    }


    @Override
    public void sortedSetRemove(String key, String value) {
        redisTemplate.opsForZSet().remove(key, value);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);

    }

    @Override
    public void sortedSetSave(String key, String usrId, long timestamp) {

        redisTemplate.opsForZSet().add(key,usrId,timestamp);
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
