package com.rand.service;

import com.rand.common.ResponseDTO;
import com.rand.config.redis.pubsub.Publisher;
import com.rand.config.redis.pubsub.SseNotificationService;
import com.rand.config.redis.pubsub.SubsCriber;
import com.rand.custom.SecurityContextGet;
import com.rand.exception.custom.InternerServerException;
import com.rand.match.dto.MatchDTO;
import com.rand.member.model.Members;
import com.rand.redis.InMemRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final InMemRepository inMemRepository;
    private static final String WAITING_QUE_KEY = "matching-queue";
    private static final String MEMBER_DISTANCE_COND_KEY = "member:distance";
    private static final String MATCH_LOCK_KEY = "match:lock";
    private static final long MATCH_LOCK_TIMEOUT = 5; // 5초 동안 대기 후 재시도
    private final Publisher publisher;
    private final SubsCriber subsCriber;
    private final SseNotificationService sseNotificationService;


    @Override
    public ResponseEntity<ResponseDTO<Void>> matchLogic(MatchDTO matchDTO) {

        boolean acquired = lockCheck(MATCH_LOCK_KEY,MATCH_LOCK_TIMEOUT);

        if (acquired) {
            try {
                double convertMeters = matchDTO.getDistance() * 1000;
                matchDTO.setDistance(convertMeters);

                Members members = new Members(matchDTO);
                members.setUsrId(SecurityContextGet.getUsrId());

                log.info("members.distance={}", members.getDistance());
                //현재 시간
                long timestamp = System.currentTimeMillis();
                String usrId = String.valueOf(members.getUsrId());

                log.info("usrid={}", usrId);
                //큐 저장
                inMemRepository.sortedSetSave(WAITING_QUE_KEY, usrId, timestamp);

                //거리 조건 저장
                double distance = members.getDistance();

                inMemRepository.hashSave(MEMBER_DISTANCE_COND_KEY, usrId, distance);

//                //sse 저장
//                sseNotificationService.connect(usrId);

                processMatchingQueue();
            } finally {
                inMemRepository.delete(MATCH_LOCK_KEY);
            }

        } else {
            throw new InternerServerException("ERR-CMN-03");
        }

        return ResponseEntity.ok(new ResponseDTO<>(null));
    }

    @Async
    private void processMatchingQueue() {
        long currentTime = System.currentTimeMillis();
        long thresholdTime = currentTime - TimeUnit.SECONDS.toMillis(60); //1분 이내 매칭 대기열 들어온 사람

        Set<String> usrIds = inMemRepository.sortedSetRangeByScore(WAITING_QUE_KEY, thresholdTime, currentTime);


        //1분 넘은 사람 제거 후 웹소켓 알람

        if (usrIds.size() < 2) {
            // 매칭할 수 있는 사용자가 충분하지 않으면 종료
            return;
        }
        // 3. 사용자들의 geo 정보를 가져와 거리 조건을 비교
        for (String firstUserId : usrIds) {
            log.info(firstUserId);
            Double firstUserDistance = (Double) inMemRepository.getHashValue(MEMBER_DISTANCE_COND_KEY, firstUserId);

            // 4. 첫 번째 사용자와 가까운 사용자들 찾기 (GeoRadius로 범위 내 사용자 조회)
            Set<String> nearbyUsers = inMemRepository.geoRadius(firstUserId, firstUserDistance);
            //교집합 (매칭 대기열과)
            nearbyUsers.retainAll(usrIds);
            // 5. 범위 내에 사용자가 있으면 두 번째 사용자와 거리 조건을 비교
            for (String secondUserId : nearbyUsers) {
                if (firstUserId.equals(secondUserId)) {
                    continue;  // 자신과 비교하지 않음
                }

                Double secondUserDistance = (Double) inMemRepository.getHashValue(MEMBER_DISTANCE_COND_KEY, secondUserId);

                // 6. 두 사용자 간의 거리 조건을 비교
                if (isDistanceConditionMet(firstUserId, firstUserDistance, secondUserId, secondUserDistance)) {
                    // 거리 조건을 만족하면 매칭 처리
                    matchUsers(firstUserId, secondUserId);


                    // 매칭이 완료된 후 더 이상 비교하지 않도록 종료
                    break;
                }
            }
        }

    }

    private boolean isDistanceConditionMet(String firstUserId, Double firstUserDistance, String secondUserId, Double secondUserDistance) {
        //두사용자와의 거리
        double distance = inMemRepository.calculateDistance(firstUserId, secondUserId);
        boolean condition = false;
        log.info("distance = {}", distance);

        if (distance <= firstUserDistance && distance <= secondUserDistance) {
            condition = true;
        }

        return condition;
    }

    private void matchUsers(String firstUserId, String secondUserId) {

        log.info("Matched users1 = {}", firstUserId);
        log.info("Matched users2 = {}", secondUserId);

        // 매칭된 사용자 제거
        inMemRepository.sortedSetRemove(WAITING_QUE_KEY, firstUserId);
        inMemRepository.sortedSetRemove(WAITING_QUE_KEY, secondUserId);

        double distance = inMemRepository.calculateDistance(firstUserId, secondUserId);

        publisher.sendNotification(firstUserId,"MATCH COMPLETE["+secondUserId+","+distance+"km]");
        publisher.sendNotification(secondUserId,"MATCH COMPLETE["+firstUserId+","+distance+"km]");

    }

    private boolean lockCheck(String LOCK_KEY, long LOCK_TIMEOUT) {
        String lockVal = String.valueOf(System.currentTimeMillis() + 10000);
        boolean acquired = false;
        long startTime = System.currentTimeMillis();

        // 락 획득 시도 (최대 10초 동안 시도)
        while (System.currentTimeMillis() - startTime < TimeUnit.SECONDS.toMillis(LOCK_TIMEOUT)) {
            acquired = inMemRepository.lockSetting(LOCK_KEY, lockVal, 10);

            if (Boolean.TRUE.equals(acquired)) {
                acquired = true;
                break;  // 락 획득 성공
            } else {
                try {
                    Thread.sleep(1000);  // 1초 대기 후 재시도
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();  // 인터럽트 처리
                }
            }
        }
        return acquired;
    }
}
