package rand.api.web.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
public enum ErrorCode {
    //타입에러

    PARAMETER_TYPE_MISMATCH(400, "ERR-CMN-01", "PARAMETER TYPE MISMATCH"),
    JSON_PARSE_ERROR  (400, "ERR-CMN-02", "JSON PARSE ERROR"),

    //회원 관련 에러코드
    VALIDATAION_FAIL_JOIN_ID(400, "ERR-MEM-01", "JOIN ID VALIDATION FAIL"),
    VALIDATAION_FAIL_JOIN_NM(400, "ERR-MEM-02", "JOIN NAME VALIDATION FAIL"),
    VALIDATAION_FAIL_JOIN_PWD(400, "ERR-MEM-03", "JOIN PWD VALIDATION FAIL"),
    VALIDATAION_FAIL_JOIN_EMAIL(400, "ERR-MEM-04", "JOIN EMAIL VALIDATION FAIL"),
    VALIDATAION_FAIL_JOIN_PHONE(400, "ERR-MEM-05", "JOIN PHONE VALIDATION FAIL");

    private final int status;
    private final String message;
    private final String code;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime  timestamp;


    ErrorCode(int status, String message,String code) {
        this.status = status;
        this.message = message;
        this.code = code;
    }


}
