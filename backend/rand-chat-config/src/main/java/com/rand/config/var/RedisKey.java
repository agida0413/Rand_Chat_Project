package com.rand.config.var;

public final class RedisKey {
    //매칭 sse 정보담는  키
    public static final String SSE_CONNECTION_KEY = "sse:match:";
    //회원 위치정보
    public static final String GEO_KEY = "member:location";
    //대기열 
    public static final String WAITING_QUE_KEY = "matching-queue";
    //회원 대기열 거리조건 
    public static final String MEMBER_DISTANCE_COND_KEY = "member:distance";
    // 매칭 락 
    public static final String MATCH_LOCK_KEY = "match:lock";
    //계정활성화 인증코드 키
    public static final String UNLOCK_ACCOUNT_KEY = "unlockAccount:";
    // 계정활성화 시도 횟수
    public static final String UNLOCK_ACCOUNT_ATTEMP_KEY = "unlockAttempt:";
    //이메일 인증이력
    public static final String AUTH_HISTORY_KEY = "authHs:";
    //회원가입 이메일 인증코드
    public static final String JOIN_EMAIL_AUTH_CODE_KEY= "authCd:";
    
    //회원가입 이메일 인증 시도 횟수
    public static final String JOIN_EMAIL_AUTH_CODE_ATTEMPT_KEY= "emailAuthAttempt:";
    //리프레시토큰
    public static final String REFRESH_TOKEN_KEY ="refresh:";

    //매칭 락 타임아웃
    public static final long MATCH_LOCK_TIMEOUT = 5; // 5초 동안 대기 후 재시도
    //멤버인포 키
    public static final String MEMBER_INFO_KEY = "memberInfo:"; // 5초 동안 대기 후 재시도

}
