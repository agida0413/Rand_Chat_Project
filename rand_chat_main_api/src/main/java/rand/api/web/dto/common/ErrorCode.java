package rand.api.web.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
public enum ErrorCode {
    //최상위 공통 에러
    COMMON_UNPREDICTABLE_ERROR(500, "ERR-CMN-01", "서버 내부 오류입니다. 관리자에게 문의하세요."),

    //타입에러 , 프론트엔드 측 메시징 금지 및 별도 처리 요망
    PARAMETER_TYPE_MISMATCH(400, "ERR-TYPE-01", "PARAMETER TYPE MISMATCH OR CLIENT GIVE UNPREDICTABLE"),
    JSON_PARSE_ERROR  (400, "ERR-TYPE-02", "JSON PARSE ERROR"),
    HTTP_MESSAGE_CANT_READ(400, "ERR-TYPE-03", "JSON PARSE ERROR OR CANT READ HTTP_MESSAGE YOU MUST CHCEK TYPE"),
    HTTP_MEDIA_TYPE_ERROR(400, "ERR-TYPE-04", "CHECK YOUR HTTP HEADER- CONTENT..ACCEPT..ELSE"),

    //Validation Fail 코드

    //회원가입 관련 에러코드
    VALIDATAION_FAIL_JOIN_ID(400, "ERR-VALID-MEM-01", "아이디는 8~16자리여야 합니다."),
    VALIDATAION_FAIL_JOIN_NNM(400, "ERR-VALID-MEM-02", "닉네임은 3~14자 한글만 가능합니다."),
    VALIDATAION_FAIL_JOIN_PWD(400, "ERR-VALID-MEM-03", "비밀번호는 형식이 맞지 않습니다.(8~16자, 특수문자 포함 및 공백불가) "),
    VALIDATAION_FAIL_JOIN_EMAIL(400, "ERR-VALID-MEM-04", "올바른 이메일 형식이 아닙니다."),
    VALIDATAION_FAIL_JOIN_SEX(400, "ERR-VALID-MEM-05", "올바른 성별 형식이 아닙니다."),
    VALIDATAION_FAIL_JOIN_BIRTH(400, "ERR-VALID-MEM-06", "올바른 생년월일 형식이 아닙니다."),

    //이메일 인증 관련 에러코드
    VALIDATAION_FAIL_EMAIL_AUTH(400, "ERR-VALID-MEM-07", "올바른 이메일 형식이 아닙니다."),
    VALIDATAION_FAIL_EMAIL_AUTH_CODE(400, "ERR-VALID-MEM-08", "올바른 인증코드 형식이 아닙니다."),


    //아이디 찾기 관련 에러코드
    VALIDATAION_FAIL_FIND_ID_EMAIL(400, "ERR-VALID-MEM-09", "올바른 이메일 형식이 아닙니다."),
    VALIDATAION_FAIL_FIND_ID_NNM(400, "ERR-VALID-MEM-10", "닉네임은 4~14자 한글만 가능합니다."),

    //사용자 정의 에러코드

    //회원 관련 에러코드
    //중복
    EMAIL_AUTH_DUPLICATE(400, "ERR-JOIN-CS-01", "중복된 이메일 입니다."),
    NICK_NAME_DUPLICATE(400, "ERR-JOIN-CS-02", "중복된 닉네임 입니다."),
    USER_NAME_DUPLICATE(400, "ERR-JOIN-CS-03", "중복된 아이디 입니다."),

    //이메일 인증 관련
    EMAIL_SEND_FAIL(500, "ERR-EAUTH-CS-01", "이메일 전송에 실패하였습니다. 관리자에게 문의하세요."),

    NOT_SEND_OR_EXPIRED_EMAIL_AUTHCODE(400, "ERR-EAUTH-CS-02", "인증 이메일을 보내지 않았거나 만료된 인증코드입니다."),
    NOT_MATCH_EMAIL_AUTHCODE(400, "ERR-EAUTH-CS-03", "이메일 인증코드가 틀렸습니다."),
    TO_MANY_REQ_EMAIL_AUTH_CHECK(400, "ERR-EAUTH-CS-04", "이메일 인증코드 입력 5회 초과입니다. 잠시 뒤 이용해주세요."),

    //회원가입  접근금지 관련
    CANT_ACCESS_JOIN(400, "ERR-EAUTH-CS-05", "인증이 만료되었습니다. 이메일 인증을 다시 해주세요."),

    //아이디 찾기 관련 에러코드
    NO_DATA_FROM_FIND_ID(400,"ERR-EAUTH-CS-06","등록된 아이디가 없습니다.");

    private final int status;
    private final String code;
    private final String message;



    ErrorCode(int status, String code,String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }


}
