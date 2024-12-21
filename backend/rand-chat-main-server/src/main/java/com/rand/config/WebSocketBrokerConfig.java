package com.rand.config;

import com.rand.interceptor.WebSocketInterCeptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {
    private final WebSocketInterCeptor webSocketInterCeptor;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

//                // CONNECT 요청 처리
//                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//                    String userId = (String) accessor.getSessionAttributes().get("usrId");
//                    if (userId != null) {
//                        // Principal에 사용자 정보 설정
//                        log.info("principal={}",userId);
//                        accessor.setUser(new UsernamePasswordAuthenticationToken(userId, null));
//                    }else{
//                        log.info("실패");
//                    }
//                }

                //토큰검증
                if (StompCommand.SEND.equals(accessor.getCommand())) {

                    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
                    String accessToken = String.valueOf(headerAccessor.getNativeHeader("access"));

                    if (accessToken == null || accessToken.equals("")) {
                        throw new MessageDeliveryException("메세지 예외");
                    }

                    log.info("pre_token={}", accessToken);
                }
                return message;
            }
        });
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat/ws")
                .addInterceptors(webSocketInterCeptor)  // 세션 핸들러 추가
                .setAllowedOrigins("*");  // 클라이언트 도메인 허용 (개발 중에는 "*" 가능)
//               .withSockJS();          // SockJS 사용
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트가 구독할 수 있는 경로 설정
        registry.enableSimpleBroker("/sub/chat");
        // 메시지를 보낼 때 사용할 prefix 설정
        registry.setApplicationDestinationPrefixes("/pub/chat");

    }


}
