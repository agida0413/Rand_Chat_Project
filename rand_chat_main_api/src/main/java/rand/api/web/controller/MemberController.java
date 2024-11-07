package rand.api.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rand.api.domain.member.service.MemberService;
import rand.api.domain.utilservice.MailService;
import rand.api.web.dto.common.ResponseDTO;
import rand.api.web.dto.member.request.EmailAuthDTO;
import rand.api.web.dto.member.request.JoinDTO;
import rand.api.web.exception.custom.BadRequestException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/member",produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {

    private final MemberService memberService;

    //이메일 검증 및 인증번호 발송 One per Service
    @PostMapping(value = "/email",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<ResponseDTO<Void>> emailAuthJoin(@Validated EmailAuthDTO emailAuthDTO){
        log.info("received email authentication request for email: {}", emailAuthDTO.getEmail());



    return memberService.emailDuplicateCheck(emailAuthDTO);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> memberJoin(@Validated JoinDTO joinDTO){


        String data="dd";
        ResponseDTO<String> responseDTO= new ResponseDTO<String>(data);
        return ResponseEntity.ok(responseDTO);
        }

}
