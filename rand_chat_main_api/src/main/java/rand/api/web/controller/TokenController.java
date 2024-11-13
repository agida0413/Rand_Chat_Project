package rand.api.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rand.api.web.dto.common.ResponseDTO;
import rand.api.web.security.service.TokenService;

@RestController
@RequestMapping(value = "/api/v1",produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;
    //비활성화 , 잠금계정 활성화 인증코드 검증 및 활성화 수행
    @GetMapping(value = "/reissue")
    public ResponseEntity<ResponseDTO<Void>> reissueToken(HttpServletRequest request, HttpServletResponse response){

        return tokenService.reissueToken(request,response);
    }
}
