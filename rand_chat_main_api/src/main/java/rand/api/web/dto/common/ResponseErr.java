package rand.api.web.dto.common;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResponseErr {
    private final int status;
    private final String message;
    private final String code;

    public ResponseErr(ErrorCode errorCode) {
        this.status=errorCode.getStatus();
        this.message=errorCode.getMessage();
        this.code=errorCode.getCode();
    }
}
