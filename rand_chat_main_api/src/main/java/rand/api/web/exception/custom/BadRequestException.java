package rand.api.web.exception.custom;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException{
    private final String errorCode;

    public BadRequestException(String errorCode) {
        super(errorCode);
        // 상수 에러코드
        this.errorCode=errorCode;
    }
}
