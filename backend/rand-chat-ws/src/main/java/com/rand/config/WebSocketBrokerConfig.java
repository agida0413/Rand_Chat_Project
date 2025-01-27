package com.rand.config;

import com.rand.exception.StompExceptionHandler;
import com.rand.handler.CustomHandShakeHandler;
import com.rand.interceptor.StompInBoundInterceptor;
import com.rand.interceptor.WebSocketInterCeptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;

import org.springframework.web.socket.config.annotation.*;

// Stomp 설정
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketInterCeptor webSocketInterCeptor;
    private final StompInBoundInterceptor chatPreHandler;
    private final StompExceptionHandler stompExceptionHandler;
    private final CustomHandShakeHandler customHandShakeHandler;
    // Stomp 통신간 인터셉터
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(chatPreHandler);
    }


//Stomp 설정
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .setErrorHandler(stompExceptionHandler)
                .addEndpoint("/chat/ws")
                .addInterceptors(webSocketInterCeptor)  // 세션 핸들러 추가
                .setAllowedOrigins("http://localhost:3000","http://randchat.o-r.kr","https://randchat.o-r.kr") // 클라이언트 도메인 허용 (개발 중에는 "*" 가능)
                .setHandshakeHandler(customHandShakeHandler);
//               .withSockJS();          // SockJS 사용
    }




    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트가 구독할 수 있는 경로 설정
        registry.enableSimpleBroker("/sub/chat","/queue");
        // 메시지를 보낼 때 사용할 prefix 설정
        registry.setApplicationDestinationPrefixes("/pub/chat");
        registry.setUserDestinationPrefix("/queue/");  // 사용자별 목적지 설정

    }


}
