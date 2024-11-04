package rand.api.web.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rand.api.web.dto.common.ErrorCode;
import rand.api.web.dto.common.ResponseErr;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    // Validation 실패 오류 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseErr> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("ValidationException: {}", ex.getMessage(), ex);

        FieldError fieldError = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);


        // 필드 오류가 존재할 경우에만 처리
        log.error("validation 실패={}",ex);

            ErrorCode errorCode=null;

            // 필드에 따라 적절한 ErrorCode를 반환
            switch (fieldError.getField()) {
                case "username":

                    if (fieldError.getObjectName().equals("joinDTO")) {
                        errorCode = ErrorCode.VALIDATAION_FAIL_JOIN_ID;
                    }
                    break;
                case "name":
                    if (fieldError.getObjectName().equals("joinDTO")) {
                        errorCode = ErrorCode.VALIDATAION_FAIL_JOIN_NM;
                    }
                    break;
                case "password":
                    if (fieldError.getObjectName().equals("joinDTO")) {
                        errorCode = ErrorCode.VALIDATAION_FAIL_JOIN_PWD;
                    }
                    break;
                case "email":
                    if (fieldError.getObjectName().equals("joinDTO")) {
                        errorCode = ErrorCode.VALIDATAION_FAIL_JOIN_EMAIL;
                    }
                    break;
                case "phone":
                    if (fieldError.getObjectName().equals("joinDTO")) {
                        errorCode = ErrorCode.VALIDATAION_FAIL_JOIN_PHONE;
                    }
                    break;
                default:
                    errorCode = null; // 매칭되는 오류가 없을 경우 null 반환
                    break;
            }

        if (errorCode==null){
            //서버 에러로
        }

        ResponseErr responseErr = new ResponseErr(errorCode);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseErr);

    }

}
