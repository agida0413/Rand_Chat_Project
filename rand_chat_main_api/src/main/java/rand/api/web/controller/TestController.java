package rand.api.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class TestController {


    @RequestMapping
    public ResponseEntity<?> test(){
        log.info("this server is run (Main server is run )");
        return ResponseEntity.ok("Main SERVER RESPONSE");
    }


}