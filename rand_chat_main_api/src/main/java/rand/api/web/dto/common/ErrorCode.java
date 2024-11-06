package rand.api.web.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
public enum ErrorCode {
    //최상위 공통 에러
    COMMON_UNPREDICTABLE_ERROR(500, "ERR-CMN-01", "UNPREDICTABLE SERVER ERROR"),
    //타입에러
    PARAMETER_TYPE_MISMATCH(400, "ERR-CMN-02", "PARAMETER TYPE MISMATCH OR CLIENT GIVE UNPREDICTABLE"),
    JSON_PARSE_ERROR  (400, "ERR-CMN-03", "JSON PARSE ERROR"),
    HTTP_MESSAGE_CANT_READ(400, "ERR-CMN-04", "JSON PARSE ERROR OR CANT READ HTTP_MESSAGE YOU MUST CHCEK TYPE"),
    HTTP_MEDIA_TYPE_ERROR(400, "ERR-CMN-05", "CHECK YOUR HTTP HEADER- CONTENT..ACCEPT..ELSE"),

    //Validation Fail 코드

    //회원가입 관련 에러코드
    VALIDATAION_FAIL_JOIN_ID(400, "ERR-MEM-01", "JOIN ID VALIDATION FAIL"),
    VALIDATAION_FAIL_JOIN_NM(400, "ERR-MEM-02", "JOIN NAME VALIDATION FAIL"),
    VALIDATAION_FAIL_JOIN_PWD(400, "ERR-MEM-03", "JOIN PWD VALIDATION FAIL"),
    VALIDATAION_FAIL_JOIN_EMAIL(400, "ERR-MEM-04", "JOIN EMAIL VALIDATION FAIL"),
    VALIDATAION_FAIL_JOIN_PHONE(400, "ERR-MEM-05", "JOIN PHONE VALIDATION FAIL"),

    //이메일 인증 관련 에러코드
    VALIDATAION_FAIL_EMAIL_AUTH(400, "ERR-MEM-06", "EMAIL_AUTH IN EMAIL VALIDATION FAIL"),


    //사용자 정의 에러코드

    //회원관련 에러코드
    EMAIL_AUTH_DUPLICATE(400, "ERR-MEM-CUS-01", "JOIN EMAIL_VALIDATE_DUPLICATED"),
    EMAIL_SEND_FAIL(400, "ERR-MEM-CUS-02", "EMAIL SEND FAIL");

    private final int status;
    private final String code;
    private final String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime  timestamp;


    ErrorCode(int status, String code,String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }


}
