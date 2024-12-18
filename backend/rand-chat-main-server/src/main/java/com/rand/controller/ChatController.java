package com.rand.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat/api")
public class ChatController {

@GetMapping
    public String test(){
    return  "ok";
}
}
