package com.rand.common;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ResponseErr {
    private final int status;
    private final String message;
    private final String code;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp=LocalDateTime.now();

    public ResponseErr(ErrorCode errorCode) {
        this.status=errorCode.getStatus();
        this.message=errorCode.getMessage();
        this.code=errorCode.getCode();
    }
}
