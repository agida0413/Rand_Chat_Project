package com.rand.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rand.config.constant.PubSubChannel;
import com.rand.config.var.RedisKey;
import com.rand.custom.SecurityResponse;
import com.rand.jwt.JWTUtil;
import com.rand.jwt.JwtError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

//웹소켓 연결시에 실행되는 인터셉터
import java.security.Principal;
import java.util.Map;
@Slf4j
@RequiredArgsConstructor
@Component
//웹소켓 연결확립 인터셉터
public class WebSocketInterCeptor implements HandshakeInterceptor {

    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;
    //핸드세이킹 이전 토큰을검증하여 차단하는 메소드 및 성공시 세션에 저장
    @Override
    public boolean beforeHandshake(org.springframework.http.server.ServerHttpRequest request, org.springframework.http.server.ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        String accessToken ="";
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            accessToken = servletRequest.getServletRequest().getParameter("access");
        }

        JwtError jwtError= jwtUtil.validate(accessToken);

        if (accessToken != null && jwtError.equals(JwtError.CORRECT)) {
            String userId = extractUserIdFromToken(accessToken);
            attributes.put("usrId", userId);  // 인증이 성공하면 userId를 WebSocket 세션에 저장
            attributes.put("access",accessToken);
            return true;
        } else {
            if(accessToken ==null){
                SecurityResponse.writeErrorRes( response,objectMapper,"ERR-SEC-12");
                throw new MessageDeliveryException("ERR-SEC-12");
            }else{
                switch (jwtError){
                    case SIGNATURE :
                        throw new MessageDeliveryException("ERR-SEC-12");
                    case ILLEGAL:
                        throw new MessageDeliveryException("ERR-SEC-12");
                    case MALFORM:
                        throw new MessageDeliveryException("ERR-SEC-12");
                    case UNSUPPORT:
                        throw new MessageDeliveryException("ERR-SEC-12");
                    case EXPIRED:
                        log.info("저기서 터짐");
                        throw new MessageDeliveryException("ERR-SEC-10");
                }
            }

            String category = jwtUtil.getCategory(accessToken);

            if (!category.equals("access")) {
                //액세스토큰이 아닐시
                throw new MessageDeliveryException("ERR-SEC-08");
            }

            return false;  // WebSocket 연결을 허용하지 않음
        }


    }

    @Override
    public void afterHandshake(org.springframework.http.server.ServerHttpRequest request, org.springframework.http.server.ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }



    private String extractUserIdFromToken(String token) {
       return String.valueOf(jwtUtil.getUsrId(token));
    }
}
