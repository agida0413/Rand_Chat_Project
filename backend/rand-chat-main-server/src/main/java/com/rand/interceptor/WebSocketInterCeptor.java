package com.rand.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rand.config.constant.PubSubChannel;
import com.rand.config.var.RedisKey;
import com.rand.custom.SecurityResponse;
import com.rand.jwt.JWTUtil;
import com.rand.jwt.JwtError;
import com.rand.redis.pubsub.ChatPubSubRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.security.Principal;
import java.util.Map;
@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketInterCeptor implements HandshakeInterceptor {

    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final ChatPubSubRegistry chatPubSubRegistry;
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
            }else{
                switch (jwtError){
                    case SIGNATURE :
                        SecurityResponse.writeErrorRes( response,objectMapper,"ERR-SEC-12");
                        break;
                    case ILLEGAL:
                        SecurityResponse.writeErrorRes( response,objectMapper,"ERR-SEC-12");
                        break;
                    case MALFORM:
                        SecurityResponse.writeErrorRes( response,objectMapper,"ERR-SEC-12");
                        break;
                    case UNSUPPORT:
                        SecurityResponse.writeErrorRes( response,objectMapper,"ERR-SEC-12");
                        break;
                    case EXPIRED:
                        SecurityResponse.writeErrorRes( response,objectMapper,"ERR-SEC-10");
                        break;
                }
            }

            String category = jwtUtil.getCategory(accessToken);

            if (!category.equals("access")) {
                //액세스토큰이 아닐시
                SecurityResponse.writeErrorRes(response,objectMapper,"ERR-SEC-08");
            }

            return false;  // WebSocket 연결을 허용하지 않음
        }


    }

    @Override
    public void afterHandshake(org.springframework.http.server.ServerHttpRequest request, org.springframework.http.server.ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

        String accessToken ="";
        String usrId="";

        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            accessToken = servletRequest.getServletRequest().getParameter("access");
        }

        if (accessToken != null) {
            usrId = extractUserIdFromToken(accessToken);

        }


            //채팅웹소켓연결 후 서버정보를 저장 - > pub/sub 을통해 연결된 인스턴스 찾기 위해
//        chatPubSubRegistry.registerConnection(usrId, PubSubChannel.CHAT_CHANNEL.toString(), RedisKey.CHAT_SOCKET_KEY);

    }



    private String extractUserIdFromToken(String token) {
       return String.valueOf(jwtUtil.getUsrId(token));
    }
}
