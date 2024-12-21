//package com.rand.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.MessageDeliveryException;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.ChannelInterceptor;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//public class ChatPreHandler implements ChannelInterceptor {
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//
//            String userId = (String) accessor.getSessionAttributes().get("usrId");
//            Map<String, Object> updatedMessage = new HashMap<>();
//
//            if (userId != null) {
//                // Principal에 사용자 정보 설정
//                updatedMessage.put("usrId", userId);
//                updatedMessage.put("originalMessage", message.getPayload());  // Preserving original message
//                message = MessageBuilder.withPayload(updatedMessage)
//                        .build();
//
//            } else {
//                throw new MessageDeliveryException("메세지 예외");
//            }
//
//        if (StompCommand.SEND.equals(accessor.getCommand())) {
//            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
//            String accessToken = String.valueOf(headerAccessor.getNativeHeader("access"));
//
//            if (accessToken == null || accessToken.equals("")) {
//                throw new MessageDeliveryException("메세지 예외");
//            }
//
//            log.info("pre_token={}", accessToken);
//        }
//        return message;
//    }
//}
