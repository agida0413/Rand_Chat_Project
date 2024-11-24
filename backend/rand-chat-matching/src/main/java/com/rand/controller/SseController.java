package com.rand.controller;

import com.rand.config.redis.pubsub.SseNotificationService;
import com.rand.custom.SecurityContextGet;
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

    @GetMapping
    public SseEmitter connect(){
      String usrId=String.valueOf(SecurityContextGet.getUsrId());
        return sseNotificationService.connect(usrId);
    }
}
