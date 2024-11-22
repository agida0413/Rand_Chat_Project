package com.rand.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
    //최상위 공통 에러
    COMMON_UNPREDICTABLE_ERROR(500, "ERR-CMN-01", "서버 내부 오류입니다. 관리자에게 문의하세요."),
    //쿠키 추출에러
    COMMON_COOKIE_ERROR(500, "ERR-CMN-02", "서버 내부 오류입니다. 관리자에게 문의하세요."),
    COMMON_LOCK_FAIL(500, "ERR-CMN-03", "락 획득 실패(타임아웃)"),

    //타입에러 , 프론트엔드 측 메시징 금지 및 별도 처리 요망
    PARAMETER_TYPE_MISMATCH(400, "ERR-TYPE-01", "PARAMETER TYPE MISMATCH OR CLIENT GIVE UNPREDICTABLE"),
    JSON_PARSE_ERROR  (400, "ERR-TYPE-02", "JSON PARSE ERROR"),
    HTTP_MESSAGE_CANT_READ(400, "ERR-TYPE-03", "JSON PARSE ERROR OR CANT READ HTTP_MESSAGE YOU MUST CHCEK TYPE"),
    HTTP_MEDIA_TYPE_ERROR(400, "ERR-TYPE-04", "CHECK YOUR HTTP HEADER- CONTENT..ACCEPT..ELSE"),

    //Validation Fail 코드

    VALIDATAION_FAIL_ID(400, "ERR-VALID-MEM-19", "올바른 아이디 형식이 아닙니다."),
    //회원가입 관련 에러코드
    VALIDATAION_FAIL_JOIN_ID(400, "ERR-VALID-MEM-01", "아이디는 8~16자리여야 합니다."),

    VALIDATAION_FAIL_JOIN_NNM(400, "ERR-VALID-MEM-02", "닉네임은 3~14자 한글만 가능합니다."),
    VALIDATAION_FAIL_JOIN_PWD(400, "ERR-VALID-MEM-03", "비밀번호는 형식이 맞지 않습니다.(8~16자, 특수문자 포함 및 공백불가) "),
    VALIDATAION_FAIL_JOIN_EMAIL(400, "ERR-VALID-MEM-04", "올바른 이메일 형식이 아닙니다."),
    VALIDATAION_FAIL_JOIN_SEX(400, "ERR-VALID-MEM-05", "올바른 성별 형식이 아닙니다."),
    VALIDATAION_FAIL_JOIN_BIRTH(400, "ERR-VALID-MEM-06", "올바른 생년월일 형식이 아닙니다."),
    //11 /22 추가
    VALIDATAION_FAIL_NAME(400, "ERR-VALID-MEM-18", "올바른 이름 형식이 아닙니다."),

    //이메일 인증 관련 에러코드
    VALIDATAION_FAIL_EMAIL_AUTH(400, "ERR-VALID-MEM-07", "올바른 이메일 형식이 아닙니다."),
    VALIDATAION_FAIL_EMAIL_AUTH_CODE(400, "ERR-VALID-MEM-08", "올바른 인증코드 형식이 아닙니다."),


    //아이디 찾기 관련 에러코드
    VALIDATAION_FAIL_FIND_ID_EMAIL(400, "ERR-VALID-MEM-09", "올바른 이메일 형식이 아닙니다."),
    VALIDATAION_FAIL_FIND_ID_NNM(400, "ERR-VALID-MEM-10", "닉네임은 4~14자 한글만 가능합니다."),


    //비밀번호 초기화 관련 에러코드
    VALIDATAION_FAIL_RESET_PWD_EMAIL(400, "ERR-VALID-MEM-11", "올바른 이메일 형식이 아닙니다."),
    VALIDATAION_FAIL_RESET_PWD_NNM(400, "ERR-VALID-MEM-12", "닉네임은 4~14자 한글만 가능합니다."),


    //계정 상태 활성화 관련 에러코드
    VALIDATAION_FAIL_UNLOCK_ACCOUNT_EMAIL(400, "ERR-VALID-MEM-13", "올바른 이메일 형식이 아닙니다."),
    
    VALIDATAION_FAIL_UNLOCK_ACCOUNT_PWD(400, "ERR-VALID-MEM-14", "비밀번호는 공백일 수 없습니다."),
    VALIDATAION_FAIL_UNLOCK_ACCOUNT_USERNAME(400, "ERR-VALID-MEM-15", "아이디는 공백일 수 없습니다."),


    //위도경도 Valdiation
    VALIDATAION_LOCALE_LAT(400, "ERR-VALID-MEM-16", "올바른 위도 형식이 아닙니다."),
    VALIDATAION_LOCALE_LOT(400, "ERR-VALID-MEM-17", "올바른 경도 형식이 아닙니다."),


    VALIDATAION_FAIL_MATCH_DISTANCE(400, "ERR-VALID-CHAT-01", "매칭거리 조건은 0.1 이상이여야 합니다."),
    //사용자 정의 에러코드

    //회원 관련 에러코드
    //중복
    EMAIL_AUTH_DUPLICATE(400, "ERR-JOIN-CS-01", "중복된 이메일 입니다."),
    NICK_NAME_DUPLICATE(400, "ERR-JOIN-CS-02", "중복된 닉네임 입니다."),
    USER_NAME_DUPLICATE(400, "ERR-JOIN-CS-03", "중복된 아이디 입니다."),

    //이메일 전송 관련
    EMAIL_SEND_FAIL(500, "ERR-EAUTH-CS-01", "이메일 전송에 실패하였습니다. 관리자에게 문의하세요."),

    NOT_SEND_OR_EXPIRED_EMAIL_AUTHCODE(400, "ERR-EAUTH-CS-02", "인증 이메일을 보내지 않았거나 만료된 인증코드입니다."),
    NOT_MATCH_EMAIL_AUTHCODE(400, "ERR-EAUTH-CS-03", "이메일 인증코드가 틀렸습니다."),
    TO_MANY_REQ_EMAIL_AUTH_CHECK(400, "ERR-EAUTH-CS-04", "이메일 인증코드 입력 5회 초과입니다. 잠시 뒤 이용해주세요."),

    //회원가입  접근금지 관련
    CANT_ACCESS_JOIN(400, "ERR-EAUTH-CS-05", "인증이 만료되었습니다. 이메일 인증을 다시 해주세요."),

    //아이디 찾기 관련 에러코드
    NO_DATA_FROM_MEMBER_BY_ID(400,"ERR-EAUTH-CS-06","등록된 아이디가 없습니다."),

    //비밀번호 초기화 관련 에러코드
    NO_DATA_FROM_MEMBER_BY_INFO(400,"ERR-EAUTH-CS-07","등록된 정보가 없습니다."),


    NO_MATCH_PWD_FROM_MEMBER(400,"ERR-EAUTH-CS-09","비밀번호가 일치하지 않습니다."),
    ALREADY_ACTIVE_FROM_MEMBER(400,"ERR-EAUTH-CS-10","이미 계정이 활성화 상태이거나 존재하지 않는 계정입니다."),


    //시큐리티
    SEC_LOGIN_FAIL(401,"ERR-SEC-01","입력한 정보가 일치하지 않습니다"),
    SEC_LOGIN_INPUT_NULL(400,"ERR-SEC-02","아이디, 비밀번호를 입력해주세요."),
    SEC_LOGIN_THIS_SUSPEND_MEM(401,"ERR-SEC-03","탈퇴한 계정입니다."),
    SEC_LOGIN_THIS_LOCK_MEM(401,"ERR-SEC-04","잠긴 계정입니다.(패스워드 5회 미일치) 계정을 활성화 해주세요."),
    SEC_LOGIN_THIS_INACTIVE_MEM(401,"ERR-SEC-05","비 활성화 계정입니다. 계정을 활성화 해주세요."),
    SEC_NO_REFRESH_TOKEN(401,"ERR-SEC-06","비 정상적인 접근입니다."), //클라이언트로 부터 받은  리프레시 토큰이 없음
    SEC_REFRESH_TOKEN_EXPIRED(401,"ERR-SEC-07","세션이 만료되었습니다."), //클라이언트로 부터 받은  리프레시 토큰의 유효기간이 지남.
    SEC_NO_MATCH_TOKEN_CATEGORY(401,"ERR-SEC-08","비 정상적인 접근입니다."), //클라이언트로 부터 받은  토큰의 카테고리(액세스,리프레시)가 맞지않음
    SEC_UN_ILLEGAL_TOKEN(401,"ERR-SEC-09","비 정상적인 접근입니다."), //비정상적인 토큰임 (지워졋어야 했던 토큰이나 등등)
    SEC_MUST_REISSUE_TOKEN(410,"ERR-SEC-10","토큰을 재발행 하세요."); //엑세스 토큰 만료 , 리프레시토큰을 통한 재발급
    
    

    private final int status;
    private final String code;
    private final String message;




    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }


}
