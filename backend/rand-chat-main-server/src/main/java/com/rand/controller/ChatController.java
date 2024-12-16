package com.rand.controller;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ChatController {

    @MessageMapping("/msg/chat/send")
    @SendTo("/chat/room")
            public String test(@Header("simpSessionAttributes") Map<String, Object> sessionAttributes){

              String userId = (String) sessionAttributes.get("usrId");
        System.out.println("User ID: " + userId);
        return "ok";
            }
}
