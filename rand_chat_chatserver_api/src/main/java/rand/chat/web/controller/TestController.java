package rand.chat.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@Slf4j
public class TestController {


    @RequestMapping
    public ResponseEntity<?> test(){
        log.info("this server is run (chat server)");
        return ResponseEntity.ok("CHATTING SERVER RESPONSE");
    }


}
