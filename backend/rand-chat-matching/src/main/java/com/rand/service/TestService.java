//package com.rand.service;
//
//import com.rand.exception.custom.InternerServerException;
//import com.rand.match.dto.request.MatchDTO;
//import com.rand.member.dto.request.CurLocationDTO;
//import com.rand.member.model.Members;
//import com.rand.redis.RedisRepositroy;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Random;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
//@Service
//@Slf4j
//public class TestService {
//    // 경기도 ~ 서울 범위
//    private static final double MIN_LAT = 37.0; // 최소 위도 (경기도 남부)
////    private static final double MAX_LAT = 37.8; // 최대 위도 (경기도 북부, 서울과 경계)
//    private static final double MIN_LON = 126.6; // 최소 경도 (경기도 서부)
//    private static final double MAX_LON = 127.5; // 최대 경도 (경기도 동부)
//
//    private static final String WAITING_QUE_KEY = "matching-queue";
//    private static final String MEMBER_DISTANCE_COND_KEY = "member:distance";
//    private static Map<String , Object > map = new HashMap<>();
//    private static Map<String , Object > memberDistanceConditionMap = new HashMap<>();
//    private static Map<String , Object> disMap = new HashMap<>();
//
//    private static final String LOCK_KEY = "match:lock";
//    private static final long LOCK_TIMEOUT = 5; // 5초 동안 대기 후 재시도
//
//    @Autowired
//    private RedisRepositroy redisRepositroy;
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
//
//    public void matchingtest(){
//
//        //usr id 생성(위도,경도)
//
//
//        for(int i =0; i<10000; i++){
//            Random random = new Random();
//            CurLocationDTO dto = new CurLocationDTO();
//            double lat = MIN_LAT + (MAX_LAT - MIN_LAT) * random.nextDouble();
//            double lon = MIN_LON + (MAX_LON - MIN_LON) * random.nextDouble();
//            dto.setLocaleLat(lat);
//            dto.setLocaleLon(lon);
//
//            map.put(String.valueOf(i),dto);
//            Members members = new Members(dto);
//            redisRepositroy.saveLoc(String.valueOf(i),lat,lon);
//
//        }
//
//        //매칭거리조건
//
//        for(int i = 0; i<10000; i ++){
//            double randomValue = 1 + Math.random() * (30 - 1);
//            double convertMeters = (long)randomValue *1000;
//            MatchDTO dto = new MatchDTO();
//            dto.setDistance(convertMeters);
//            memberDistanceConditionMap.put(String.valueOf(i),dto);
//            redisRepositroy.hashSave(MEMBER_DISTANCE_COND_KEY,String.valueOf(i),convertMeters);
//
//
//        }
//
//        //매2
//        for (int i = 0; i<10000; i++){
//            String lockVal = String.valueOf(System.currentTimeMillis() + 10000);
//            boolean acquired = false;
//            long startTime = System.currentTimeMillis();
//
//            // 락 획득 시도 (최대 10초 동안 시도)
//            while (System.currentTimeMillis() - startTime < TimeUnit.SECONDS.toMillis(LOCK_TIMEOUT)) {
//                acquired = redisRepositroy.lockSetting(LOCK_KEY,lockVal,10);
//
//                if (Boolean.TRUE.equals(acquired)) {
//                    break;  // 락 획득 성공
//                } else {
//                    try {
//                        Thread.sleep(1000);  // 1초 대기 후 재시도
//                    } catch (InterruptedException e) {
//                        Thread.currentThread().interrupt();  // 인터럽트 처리
//                    }
//                }
//            }
//
//            if (acquired) {
//                try {
//                    MatchDTO dto = (MatchDTO)memberDistanceConditionMap.get(String.valueOf(i));
//                    Members members = new Members(dto);
//
//                    members.setUsrId(i);
//                    //현재 시간
//                    long timestamp = System.currentTimeMillis();
//                    String usrId = String.valueOf(members.getUsrId());
//
//                    //큐 저장
//                    redisRepositroy.sortedSetSave(WAITING_QUE_KEY,usrId,timestamp);
//                    processMatchingQueue();
//                }finally {
//                        redisRepositroy.delete(LOCK_KEY);
//                }
//            }
//            else{
//                throw new InternerServerException("ERR-CMN-03");
//            }
//
//        }
//
//
//
//
//
//    }
//
//    @Async
//    private void processMatchingQueue(){
//
//        long currentTime = System.currentTimeMillis();
//        long thresholdTime = currentTime - TimeUnit.SECONDS.toMillis(60); //1분 이내 매칭 대기열 들어온 사람
//
//        Set<String> usrIds = redisRepositroy.sortedSetRangeByScore(WAITING_QUE_KEY, thresholdTime, currentTime);
//
//
//        //1분 넘은 사람 제거 후 웹소켓 알람
//
//        if (usrIds.size() < 2) {
//            // 매칭할 수 있는 사용자가 충분하지 않으면 종료
//            return;
//        }
//        // 3. 사용자들의 geo 정보를 가져와 거리 조건을 비교
//        for (String firstUserId : usrIds) {
//
//            Double firstUserDistance = (Double)redisRepositroy.getHashValue(MEMBER_DISTANCE_COND_KEY, firstUserId);
//
//            // 4. 첫 번째 사용자와 가까운 사용자들 찾기 (GeoRadius로 범위 내 사용자 조회)
//            Set<String> nearbyUsers = redisRepositroy.geoRadius(firstUserId, firstUserDistance);
//
//            // 5. 범위 내에 사용자가 있으면 두 번째 사용자와 거리 조건을 비교
//            for (String secondUserId : nearbyUsers) {
//                if (firstUserId.equals(secondUserId)) {
//                    continue;  // 자신과 비교하지 않음
//                }
//
//                Double secondUserDistance = (Double) redisRepositroy.getHashValue(MEMBER_DISTANCE_COND_KEY, secondUserId);
//
//                // 6. 두 사용자 간의 거리 조건을 비교
//                if (isDistanceConditionMet(firstUserId, firstUserDistance, secondUserId, secondUserDistance)) {
//                    // 거리 조건을 만족하면 매칭 처리
//                    matchUsers(firstUserId, secondUserId);
//
//
//                    // 매칭이 완료된 후 더 이상 비교하지 않도록 종료
//                    break;
//                }
//            }
//        }
//
//    }
//
//    private boolean isDistanceConditionMet(String firstUserId, Double firstUserDistance, String secondUserId, Double secondUserDistance) {
//        //두사용자와의 거리
//
//
//        double distance = redisRepositroy.calculateDistance(firstUserId,secondUserId);
//        boolean condition = false;
//
//
//        if(distance <= firstUserDistance && distance <= secondUserDistance){
//            condition =true;
//            disMap.put((firstUserId +secondUserId),distance);
//        }
//
//        return condition;
//    }
//
//    private void matchUsers(String firstUserId, String secondUserId) {
//       log.info("match users = {}" , firstUserId + " and "+secondUserId);
//        log.info("Matched users1 = {}" , firstUserId);
//        log.info("Matched users2 = {}" , secondUserId);
//        MatchDTO dto1 = (MatchDTO) memberDistanceConditionMap.get(firstUserId);
//        MatchDTO dto2 = (MatchDTO) memberDistanceConditionMap.get(secondUserId);
//        log.info("Matched users1 condition  = {}" , dto1.getDistance()/1000);
//        log.info("Matched users2 condition  = {}" , dto2.getDistance()/1000);
//
//
//        double distanc =(double) disMap.get(firstUserId+secondUserId);
//        log.info("Matched distance = {}" , distanc/1000);
//        // 매칭된 사용자 제거
//        redisRepositroy.sortedSetRemove(WAITING_QUE_KEY, firstUserId);
//
//        redisRepositroy.sortedSetRemove(WAITING_QUE_KEY, secondUserId);
//    }
//}
