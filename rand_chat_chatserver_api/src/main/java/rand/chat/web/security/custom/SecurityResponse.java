package rand.chat.web.security.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import rand.chat.web.dto.common.ErrorCode;
import rand.chat.web.dto.common.ResponseDTO;
import rand.chat.web.dto.common.ResponseErr;


import java.io.IOException;




public final class SecurityResponse {




    public static void writeSuccessRes(HttpServletResponse response,ObjectMapper objectMapper) throws IOException {
        ResponseDTO<Void> responseDTO = new ResponseDTO<>(null);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write(objectMapper.writeValueAsString(responseDTO));


    }


    public static void writeErrorRes(HttpServletResponse response,ObjectMapper objectMapper,String errCode) throws IOException {

        ErrorCode errorCode = null;

        errorCode = selectErrorCode(errCode);

        ResponseErr responseErr = new ResponseErr(errorCode);

        response.setStatus(errorCode.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write(objectMapper.writeValueAsString(responseErr));


    }

    private static ErrorCode selectErrorCode(String strErrorCode){
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
