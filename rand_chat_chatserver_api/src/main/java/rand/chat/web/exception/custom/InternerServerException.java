package rand.chat.web.exception.custom;

import lombok.Getter;

@Getter
public class InternerServerException extends RuntimeException{

    private final String errorCode;

    public InternerServerException(String errorCode) {
        super(errorCode);
        this.errorCode=errorCode;
    }
}
