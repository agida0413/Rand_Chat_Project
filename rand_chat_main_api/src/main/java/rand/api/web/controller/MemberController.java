package rand.api.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rand.api.domain.member.service.MemberService;
import rand.api.web.dto.common.ResponseDTO;
import rand.api.web.dto.member.request.EmailAuthCheckDTO;
import rand.api.web.dto.member.request.EmailAuthSendDTO;
import rand.api.web.dto.member.request.FindIdDTO;
import rand.api.web.dto.member.request.JoinDTO;
import rand.api.web.dto.member.response.ResFindIdDTO;
import rand.api.web.exception.custom.BadRequestException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/member",produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {

    private final MemberService memberService;

    //이메일 인증번호 발송 One per Service
    @PostMapping(value = "/email",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<ResponseDTO<Void>> emailAuthJoin(@Validated EmailAuthSendDTO emailAuthDTO){
        log.info("received email authentication request for email: {}", emailAuthDTO.getEmail());

    return memberService.emailAuthSend(emailAuthDTO);
    }


    //이메일 인증번호 검증
    @PostMapping(value = "/email/check",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<ResponseDTO<Void>> emailAuthJoinCheck(@Validated EmailAuthCheckDTO emailAuthCheckDTO){
        log.info("received email authentication request for email: {}", emailAuthCheckDTO.getEmail());

        return memberService.emailAuthCheck(emailAuthCheckDTO);
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<ResponseDTO<Void>> join(@Validated JoinDTO joinDTO ){

        log.info("joindto={}",joinDTO.getBirth());
        log.info("joindto={}",joinDTO.getSex());
        log.info("joindto={}",joinDTO.getNickName());
        log.info("joindto={}",joinDTO.getEmail());
        log.info("joindto={}",joinDTO.getUsername());
        log.info("joindto={}",joinDTO.getPassword());

        return memberService.join(joinDTO);
        }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<ResFindIdDTO>> findId(@Validated @RequestBody FindIdDTO findIdDTO){

        return  memberService.findId(findIdDTO);
    }
}
