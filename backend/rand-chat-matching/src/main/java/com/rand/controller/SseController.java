package com.rand.controller;


import com.rand.config.constant.PubSubChannel;
import com.rand.config.var.RedisKey;
import com.rand.custom.SecurityContextGet;
import com.rand.exception.custom.BadRequestException;
import com.rand.jwt.JWTUtil;
import com.rand.redis.pubsub.Publisher;
import com.rand.redis.pubsub.SseNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/match/sse")
public class SseController {
private final SseNotificationService sseNotificationService;
    private final Publisher publisher;
    private final JWTUtil jwtUtil;
    @GetMapping
    public SseEmitter matchingConnect(){
      String usrId=String.valueOf(SecurityContextGet.getUsrId());
        return sseNotificationService.connect(usrId,PubSubChannel.MATCHING_CHANNEL.toString(), RedisKey.SSE_MATCHING_CONNECTION_KEY);
    }

    @GetMapping("/accept")
    public SseEmitter matchingAccept(@RequestHeader(value = "matchToken",required = false) String matchToken){
        //매칭 토큰이 없을 시 
        if(matchToken ==null){
            throw new BadRequestException("ERR-SEC-06"); //비정상적인 접근입니다.
        }
        
        String usrId = String.valueOf(SecurityContextGet.getUsrId());
        //매칭토큰
        String connectionKey = matchToken+":"+usrId;

        //정합성 검사
        if(!jwtUtil.getFirstUserId(matchToken).equals(usrId)){
            if(!jwtUtil.getSecondUserId(matchToken).equals(usrId)){
                //매칭토큰에 포함된 유저 2명과 현재 로그인유저 정보가 일치하지않음
                throw new BadRequestException("ERR-SEC-11"); //토큰과 로그인 정보가 일치하지 않음.
            }
        }

        return sseNotificationService.connect(connectionKey,PubSubChannel.MATCHING_ACCEPT_CHANNEL.toString(), RedisKey.SSE_MATCHING_ACCEPT_CONNECTION_KEY);
    }
   
}
