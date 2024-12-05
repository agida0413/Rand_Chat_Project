package com.rand.controller;


import com.rand.config.constant.PubSubChannel;
import com.rand.config.var.RedisKey;
import com.rand.custom.SecurityContextGet;
import com.rand.redis.pubsub.Publisher;
import com.rand.redis.pubsub.SseNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/match/sse")
public class SseController {
private final SseNotificationService sseNotificationService;
    private final Publisher publisher;
    @GetMapping
    public SseEmitter matchingConnect(){
      String usrId=String.valueOf(SecurityContextGet.getUsrId());
        return sseNotificationService.connect(usrId,PubSubChannel.MATCHING_CHANNEL.toString(), RedisKey.SSE_MATCHING_CONNECTION_KEY);
    }

    @GetMapping("/accept")
    public SseEmitter matchingAccept(){
        String usrId=String.valueOf(SecurityContextGet.getUsrId());

        return sseNotificationService.connect(usrId,PubSubChannel.MATCHING_ACCEPT_CHANNEL.toString(), RedisKey.SSE_MATCHING_ACCEPT_CONNECTION_KEY);
    }
    @GetMapping("/test")
    public void tt(){
        publisher.SendMatchingAcceptNotify(String.valueOf(SecurityContextGet.getUsrId()), PubSubChannel.MATCHING_ACCEPT_CHANNEL.toString());

    }
}
