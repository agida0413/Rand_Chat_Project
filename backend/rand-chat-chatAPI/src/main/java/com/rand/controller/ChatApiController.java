package com.rand.controller;

import com.rand.service.ChatApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatApiController {
    private final ChatApiService chatApiService;


}
