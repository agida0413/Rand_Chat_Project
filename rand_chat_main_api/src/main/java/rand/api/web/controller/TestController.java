package rand.api.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rand.api.web.dto.common.ResponseDTO;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    @GetMapping
    public ResponseEntity<ResponseDTO<Void>> tes(){
        return ResponseEntity.ok(null);
    }
}
