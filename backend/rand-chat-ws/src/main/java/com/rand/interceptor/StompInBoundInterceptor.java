package com.rand.interceptor;

import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.constant.ChatConst;
import com.rand.jwt.JWTUtil;
import com.rand.jwt.JwtError;
import com.rand.service.ChatIOService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
//STOMP 통신 인터셉터
public class StompInBoundInterceptor implements ChannelInterceptor {
    private final JWTUtil jwtUtil;
    private final ChatIOService chatWebFluxService;

//메시지 전송전 실행되어 핸들링
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

               //에러객체 전달
               List<String> errorList = new ArrayList<>();

                 //토큰검증 구독 and send
                 if (StompCommand.SEND.equals(accessor.getCommand()) || StompCommand.SUBSCRIBE.equals(accessor.getCommand()) ) {

                     StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
                     //구독이나 메시지전송간 지속적으로 보내는 토큰
                     List<String> accessTokenList = headerAccessor.getNativeHeader("access");
                     String accessToken = (accessTokenList != null && !accessTokenList.isEmpty()) ? accessTokenList.get(0) : null;


                     JwtError jwtError =JwtError.CORRECT;

                     if (accessToken == null) {
                         errorList.add("ERR-SEC-12");
                     } else {
                         //토큰검증
                          jwtError= jwtUtil.validate(accessToken);

                         switch (jwtError) {
                             case SIGNATURE:
                             case ILLEGAL:
                             case MALFORM:
                             case UNSUPPORT:
                                 errorList.add("ERR-SEC-12");
                                 break;
                             case EXPIRED:
                                 errorList.add("ERR-SEC-10");
                                 break;
                             default:
                                 break;
                         }

                     }
                    if(accessToken!=null && jwtError.equals(JwtError.CORRECT)){
                        String category = jwtUtil.getCategory(accessToken);

                        if (!category.equals("access")) {
                            //액세스토큰이 아닐시
                            errorList.add("ERR-SEC-08");
                        }
                        //리팩토링 예정 2025-01-01 : 유효기간지나서 토큰에서 추출불가함. (레디스 리프레시토큰에서 가져올까?)
//                        //세션아이디와 토큰추출아이디가 다름
//                        Principal principal = accessor.getUser();
//                        String sessionId=extractUserIdFromToken(principal.getName());
//                        String extractId=extractUserIdFromToken(accessToken);
//
//                        if(!sessionId.equals(extractId)){
//                            errorList.add("ERR-SEC-13");
//                        }

                    }

                 }



        //채팅방이 실제로 현재 세션유저가 참여하고잇는지, 존재하는지 확인하기위해 WebFlux 서비스 호출
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand()) ){
            String destination = accessor.getDestination();

            if (destination != null && destination.startsWith(ChatConst.PUB_CHAT_ROOM_URL)) {
                String roomId = destination.substring(ChatConst.PUB_CHAT_ROOM_URL.length());

                List<String> accessTokenList = accessor.getNativeHeader("access");
                String accessToken = (accessTokenList != null && !accessTokenList.isEmpty()) ? accessTokenList.get(0) : null;
                String usrId=extractUserIdFromToken(accessToken);

                RoomValidDTO roomValidDTO = new RoomValidDTO();
                roomValidDTO.setChatRoomId(roomId);
                roomValidDTO.setUsrId(usrId);
                Boolean isRealChatRoom = chatWebFluxService.isRealYourChatRoom(roomValidDTO,accessToken);

                if(isRealChatRoom==null || !isRealChatRoom){
                    throw new MessageDeliveryException("ERR-WS-01");
                }
            }

        }
        accessor.setHeader("errorList",errorList);

        return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());    }

    private String extractUserIdFromToken(String token) {
        return String.valueOf(jwtUtil.getUsrId(token));
    }
}
