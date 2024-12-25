package com.rand.interceptor;

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
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompInBoundInterceptor implements ChannelInterceptor {
    private final JWTUtil jwtUtil;

//메시지 전송전 실행되어 핸들링
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);


        //토큰검증 구독 and send
        if (StompCommand.SEND.equals(accessor.getCommand()) || StompCommand.SUBSCRIBE.equals(accessor.getCommand()) ) {
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
            //구독이나 메시지전송간 지속적으로 보내는 토큰
            List<String> accessTokenList = headerAccessor.getNativeHeader("access");
            String accessToken = (accessTokenList != null && !accessTokenList.isEmpty()) ? accessTokenList.get(0) : null;


            //토큰검증
            JwtError jwtError= jwtUtil.validate(accessToken);

                if (accessToken == null) {
                    throw new MessageDeliveryException("ERR-SEC-12");
                } else {


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
                //세션아이디와 토큰추출아이디가 다름
                Principal principal = accessor.getUser();
                String sessionId=extractUserIdFromToken(principal.getName());
                String extractId=extractUserIdFromToken(accessToken);

                if(!sessionId.equals(extractId)){
                    throw new MessageDeliveryException("ERR-SEC-13");
                }

        }

        return message;
    }

    private String extractUserIdFromToken(String token) {
        return String.valueOf(jwtUtil.getUsrId(token));
    }
}
