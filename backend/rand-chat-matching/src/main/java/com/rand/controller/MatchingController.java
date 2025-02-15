package com.rand.controller;

import com.rand.common.ResponseDTO;
import com.rand.match.dto.request.MatchAcceptDTO;
import com.rand.match.dto.request.MatchDTO;
import com.rand.match.dto.response.ResMatchAcceptDTO;
import com.rand.service.MatchAcceptService;
import com.rand.service.MatchAcceptServiceImpl;
import com.rand.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/match",produces = MediaType.APPLICATION_JSON_VALUE)
public class MatchingController {

    private final MatchService matchService;
    private final MatchAcceptService matchAcceptService;

    @GetMapping
    public ResponseEntity<ResponseDTO<Void>> matchUser(@Validated @ModelAttribute MatchDTO matchDTO){
        return  matchService.matchLogic(matchDTO);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<ResponseDTO<ResMatchAcceptDTO>> matchAccept(@RequestBody MatchAcceptDTO matchAcceptDTO
            , @RequestHeader(value = "matchToken",required = false) String matchToken){
        return  matchAcceptService.matchAccept(matchAcceptDTO,matchToken);
    }
    @PutMapping
    public ResponseEntity<ResponseDTO<Void>> matChCancle(){
        return  matchService.matchCancle();
    }


//    @GetMapping("/test")
//    public String test(){
//        long startTime = System.currentTimeMillis(); // 또는 System.nanoTime()
//
//        testService.matchingtest();
//        // 실행 종료 시간 기록
//        long endTime = System.currentTimeMillis(); // 또는 System.nanoTime()
//
//        long duration = endTime - startTime;
//
//        log.info("종료시간 = {} ",duration);
//        return "OK";
//    }
}
