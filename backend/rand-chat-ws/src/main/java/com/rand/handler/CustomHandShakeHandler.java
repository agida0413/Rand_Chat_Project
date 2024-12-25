package com.rand.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Component
@Slf4j
public class CustomHandShakeHandler extends DefaultHandshakeHandler {
    // 웹소켓 최초연결 시에 연결시점에 보낸 토큰을 Principal 세션에 저장
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {

        String access = (String) attributes.get("access");
        return new StompPrincipal(access);

    }
}
