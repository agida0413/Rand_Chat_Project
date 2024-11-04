package rand.api.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rand.api.web.dto.common.ErrorCode;
import rand.api.web.dto.common.ResponseDTO;
import rand.api.web.dto.member.JoinDTO;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {



    @PostMapping
    public ResponseEntity<ResponseDTO> memberJoin(@Validated JoinDTO joinDTO){

        log.info("joinDTO.username={}",joinDTO.getUsername());
        log.info("joinDTO.name={}",joinDTO.getName());
        log.info("joinDTO.password",joinDTO.getPassword());
        log.info("joinDTO.email",joinDTO.getEmail());
        log.info("joinDTO.phone",joinDTO.getPhone());

        String data="dd";
        ResponseDTO<String> responseDTO= new ResponseDTO<String>(data);
        return ResponseEntity.ok(responseDTO);
        }

}
