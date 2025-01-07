package com.rand.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rand.common.ErrorCode;
import com.rand.common.ResponseErr;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;


@Slf4j
@Component
@RequiredArgsConstructor
public class StompExceptionHandler extends StompSubProtocolErrorHandler {

    private final ObjectMapper objectMapper;
    private static final byte[] EMPTY_PAYLOAD = new byte[0];
    //웹소켓 에러 핸들러
    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

        ErrorCode errorCode = null;
        String errCode = ex.getMessage();
        errorCode = selectErrorCode(errCode);

        ResponseErr responseErr = new ResponseErr(errorCode);
        try {
            String responseErrStr = objectMapper.writeValueAsString(responseErr);
            accessor.setMessage(responseErrStr);
            accessor.setLeaveMutable(true);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        StompHeaderAccessor clientHeaderAccessor = null;
        if (clientMessage != null) {
            clientHeaderAccessor = MessageHeaderAccessor.getAccessor(clientMessage, StompHeaderAccessor.class);
            if (clientHeaderAccessor != null) {
                String receiptId = clientHeaderAccessor.getReceipt();
                if (receiptId != null) {
                    accessor.setReceiptId(receiptId);
                }
            }
        }
        return handleInternal(accessor, EMPTY_PAYLOAD, ex, clientHeaderAccessor);
    }

    @Override
    public Message<byte[]> handleErrorMessageToClient(Message<byte[]> errorMessage) {
        return super.handleErrorMessageToClient(errorMessage);
    }

    @Override
    protected Message<byte[]> handleInternal(StompHeaderAccessor errorHeaderAccessor, byte[] errorPayload, Throwable cause, StompHeaderAccessor clientHeaderAccessor) {
        String newPayload = errorHeaderAccessor.getMessage();
        return MessageBuilder.createMessage(newPayload.getBytes(), errorHeaderAccessor.getMessageHeaders());
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
