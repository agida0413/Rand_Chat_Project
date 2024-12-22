package com.rand.config;

import com.rand.custom.SecurityResponse;
import com.rand.jwt.JWTUtil;
import com.rand.jwt.JwtError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatPreHandler implements ChannelInterceptor {
    private final JWTUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand().equals(StompCommand.CONNECT)) {
            List<String> accessTokenList = accessor.getNativeHeader("access");
            String accessToken = (accessTokenList != null && !accessTokenList.isEmpty()) ? accessTokenList.get(0) : null;
            String usrId= extractUserIdFromToken(accessToken);


        }

        //토큰검증
        if (StompCommand.SEND.equals(accessor.getCommand()) || StompCommand.SUBSCRIBE.equals(accessor.getCommand()) ) {
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

            List<String> accessTokenList = headerAccessor.getNativeHeader("access");
            String accessToken = (accessTokenList != null && !accessTokenList.isEmpty()) ? accessTokenList.get(0) : null;

            log.info("header={}",accessToken);

            JwtError jwtError= jwtUtil.validate(accessToken);


                if (accessToken == null) {
                    log.info("errror1");
                    throw new MessageDeliveryException("ERR-SEC-12");


                } else {
                    log.info("errror2");
                    switch (jwtError) {
                        case SIGNATURE:
                            throw new MessageDeliveryException("ERR-SEC-12");
                        case ILLEGAL:
                            throw new MessageDeliveryException("ERR-SEC-12");
                        case MALFORM:
                            throw new MessageDeliveryException("ERR-SEC-12");
                        case UNSUPPORT:
                            throw new MessageDeliveryException("ERR-SEC-12");
                        case EXPIRED:
                            throw new MessageDeliveryException("ERR-SEC-10");
                    }

                }

                String category = jwtUtil.getCategory(accessToken);

                if (!category.equals("access")) {
                    //액세스토큰이 아닐시
                    throw new MessageDeliveryException("ERR-SEC-08");
                }
            log.info("pre_token={}", accessToken);

        }





        return message;
    }

    private String extractUserIdFromToken(String token) {
        return String.valueOf(jwtUtil.getUsrId(token));
    }
}
