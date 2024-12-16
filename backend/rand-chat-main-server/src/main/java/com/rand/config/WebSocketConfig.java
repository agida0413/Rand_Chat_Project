package com.rand.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketInterCeptor webSocketInterCeptor;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat/ws")
                .addInterceptors(webSocketInterCeptor)
                .setAllowedOrigins("*")  // 클라이언트 도메인 허용 (개발 중에는 "*" 가능)
                .withSockJS();          // SockJS 사용
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트가 구독할 수 있는 경로 설정
        registry.enableSimpleBroker("/chat");
        // 메시지를 보낼 때 사용할 prefix 설정
        registry.setApplicationDestinationPrefixes("/msg");

    }
}
