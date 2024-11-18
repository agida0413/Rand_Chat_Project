package com.rand.controller;

import com.rand.common.ResponseDTO;
import com.rand.match.dto.MatchDTO;
import com.rand.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/match",produces = MediaType.APPLICATION_JSON_VALUE)
public class MatchingController {

    private final MatchService matchService;
    @GetMapping
    public ResponseEntity<ResponseDTO<Void>> matchUser(@Validated @ModelAttribute MatchDTO matchDTO){
        return  matchService.matchLogic(matchDTO);
    }

}
