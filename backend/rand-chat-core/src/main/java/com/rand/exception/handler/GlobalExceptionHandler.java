package com.rand.exception.handler;

import com.rand.common.ErrorCode;
import com.rand.common.ResponseErr;
import com.rand.exception.custom.BadRequestException;
import com.rand.exception.custom.InternerServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseErr> handleCustomBadRequestException(BadRequestException ex) {
        log.error("BadRequestException: {}", ex.getMessage(), ex);


        String strErrorCode=  ex.getErrorCode();

        ErrorCode errorCode=selectErrorCode(strErrorCode);


        ResponseErr responseErr = new ResponseErr(errorCode);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseErr);

    }

    @ExceptionHandler(InternerServerException.class)
    public ResponseEntity<ResponseErr> handleCustomInternerServerException(InternerServerException ex) {
        log.error("InternerServerException: {}", ex.getMessage(), ex);


        String strErrorCode=  ex.getErrorCode();

        ErrorCode errorCode=selectErrorCode(strErrorCode);

        ResponseErr responseErr = new ResponseErr(errorCode);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseErr);

    }

    @ExceptionHandler(HttpMediaTypeException.class)
    public ResponseEntity<ResponseErr> handleCustomBadRequestException(HttpMediaTypeException ex) {
        log.error("HttpMediaTypeException: {}", ex.getMessage(), ex);




        ErrorCode errorCode=ErrorCode.HTTP_MEDIA_TYPE_ERROR;


        ResponseErr responseErr = new ResponseErr(errorCode);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseErr);

    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseErr> handleCustomBadRequestException(HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException: {}", ex.getMessage(), ex);



        ErrorCode errorCode=ErrorCode.HTTP_MESSAGE_CANT_READ;


        ResponseErr responseErr = new ResponseErr(errorCode);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseErr);

    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseErr> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error("MethodArgumentTypeMismatchException: {}", ex.getMessage(), ex);


        ErrorCode errorCode=ErrorCode.PARAMETER_TYPE_MISMATCH;


        ResponseErr responseErr = new ResponseErr(errorCode);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseErr);

    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<ResponseErr> handleTypeMismatchException(JsonParseException ex) {
        log.error("JsonParseException: {}", ex.getMessage(), ex);


        ErrorCode errorCode=ErrorCode.JSON_PARSE_ERROR;


        ResponseErr responseErr = new ResponseErr(errorCode);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseErr);

    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseErr> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("ValidationException: {}", ex.getMessage(), ex);

        FieldError fieldError = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);

        //에러 셀렉트
        ErrorCode errorCode=selecteErrorCodeInValidate(fieldError);

        ResponseErr responseErr = new ResponseErr(errorCode);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseErr);

    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseErr> handleTypeMismatchException(Exception ex) {
        log.error("Exception: {}", ex.getMessage(), ex);


        ErrorCode errorCode=ErrorCode.COMMON_UNPREDICTABLE_ERROR;


        ResponseErr responseErr = new ResponseErr(errorCode);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseErr);

    }
    //Validation In Errorcode SELECT
    private ErrorCode selecteErrorCodeInValidate(FieldError fieldError){
        ErrorCode errorCode=null;

        // 필드에 따라 적절한 ErrorCode를 반환
        switch (fieldError.getField()) {
            case "username":

                if (fieldError.getObjectName().equals("joinDTO")) {
                    errorCode = ErrorCode.VALIDATAION_FAIL_JOIN_ID;
                }
                if (fieldError.getObjectName().equals("unlockAccountDTO")) {
                    errorCode = ErrorCode.VALIDATAION_FAIL_UNLOCK_ACCOUNT_USERNAME;
                }
                break;
            case "nickName":
                if (fieldError.getObjectName().equals("joinDTO")) {
                    errorCode = ErrorCode.VALIDATAION_FAIL_JOIN_NNM;
                }
                if (fieldError.getObjectName().equals("findIdDTO")) {
                    errorCode = ErrorCode.VALIDATAION_FAIL_FIND_ID_NNM;
                }
                if (fieldError.getObjectName().equals("resetPwdDTO")) {
                    errorCode = ErrorCode.VALIDATAION_FAIL_RESET_PWD_NNM;
                }
                break;
            case "password":
                if (fieldError.getObjectName().equals("joinDTO")) {
                    errorCode = ErrorCode.VALIDATAION_FAIL_JOIN_PWD;
                }
                if (fieldError.getObjectName().equals("unlockAccountDTO")) {
                    errorCode = ErrorCode.VALIDATAION_FAIL_UNLOCK_ACCOUNT_PWD;
                }
                break;
            case "email":
                if (fieldError.getObjectName().equals("joinDTO")) {
                    errorCode = ErrorCode.VALIDATAION_FAIL_JOIN_EMAIL;
                }
                if (fieldError.getObjectName().equals("emailAuthSendDTO") || fieldError.getObjectName().equals("emailAuthCheckDTO")) {
                    errorCode = ErrorCode.VALIDATAION_FAIL_EMAIL_AUTH;
                }
                if (fieldError.getObjectName().equals("findIdDTO")) {
                    errorCode = ErrorCode.VALIDATAION_FAIL_FIND_ID_EMAIL;
                }
                if (fieldError.getObjectName().equals("resetPwdDTO")) {
                    errorCode = ErrorCode.VALIDATAION_FAIL_RESET_PWD_EMAIL;
                }
                if (fieldError.getObjectName().equals("unlockAccountDTO") || fieldError.getObjectName().equals("unlockAccountChkDTO")) {
                    errorCode = ErrorCode.VALIDATAION_FAIL_UNLOCK_ACCOUNT_EMAIL;
                }


                break;
            case "sex":
                if (fieldError.getObjectName().equals("joinDTO")) {
                    errorCode = ErrorCode.VALIDATAION_FAIL_JOIN_SEX;
                }
                break;
            case "birth":
                if (fieldError.getObjectName().equals("joinDTO")) {
                    errorCode = ErrorCode.VALIDATAION_FAIL_JOIN_BIRTH;
                }
                break;
            case "authCode":
                if (fieldError.getObjectName().equals("emailAuthCheckDTO") || fieldError.getObjectName().equals("unlockAccountChkDTO")) {
                    errorCode = ErrorCode.VALIDATAION_FAIL_EMAIL_AUTH_CODE;
                }
                break;


            case "localeLat":

                    errorCode = ErrorCode.VALIDATAION_LOCALE_LAT;

                break;
            case "localeLon":

                errorCode = ErrorCode.VALIDATAION_LOCALE_LOT;

                break;
            case "distance":
                if (fieldError.getObjectName().equals("matchDTO")) {
                    errorCode = ErrorCode.VALIDATAION_FAIL_MATCH_DISTANCE;
                }
                break;
            default:
                errorCode = null; // 매칭되는 오류가 없을 경우 null 반환
                break;
        }

        if (errorCode==null){
            errorCode = ErrorCode.COMMON_UNPREDICTABLE_ERROR;
        }
        return errorCode;
    }


    private ErrorCode selectErrorCode(String strErrorCode){
        ErrorCode selectedErrorCode = null;
        for(ErrorCode errorCode : ErrorCode.values()){
            if(errorCode.getCode().equals(strErrorCode)){
                selectedErrorCode=errorCode;
                break;
            }
        }
        if(selectedErrorCode==null){
            selectedErrorCode=ErrorCode.COMMON_UNPREDICTABLE_ERROR;
        }

        return  selectedErrorCode;
    }

}
