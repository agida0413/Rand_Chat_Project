package com.rand.controller;

import com.rand.common.ResponseDTO;
import com.rand.member.dto.request.*;
import com.rand.member.dto.response.ResFindIdDTO;
import com.rand.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



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

    //회원가입
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
    //아이디 찾기
    @GetMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<ResponseDTO<ResFindIdDTO>> findId(@Validated FindIdDTO findIdDTO){

        return  memberService.findId(findIdDTO);
    }
    //비밀번호 초기화 이메일 발송 , 업데이트
    @PutMapping(consumes =  MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<ResponseDTO<Void>> resetPwd(@Validated ResetPwdDTO resetPwdDTO){

        return memberService.resetPwd(resetPwdDTO);
    }
    //비활성화 , 잠금계정 활성화 인증코드 보내기
    @PostMapping(value = "/unlock",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<ResponseDTO<Void>> unlockAccountEmailSend(@Validated UnlockAccountDTO unlockAccountDTO){

        return memberService.emailUnlockAccountSend(unlockAccountDTO);
    }

    //비활성화 , 잠금계정 활성화 인증코드 검증 및 활성화 수행
    @PutMapping(value = "/unlock",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<ResponseDTO<Void>> unlockAccount(@Validated UnlockAccountChkDTO unlockAccountChkDTO){

        return memberService.emailUnlockAccount(unlockAccountChkDTO);
    }

    //현재 사용자 위치 업데이트
    @PutMapping(value = "/location",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<Void>> curLocUpdate(@RequestBody @Validated CurLocationDTO curLocationDTO){
        return  memberService.memberCurLocationUpdate(curLocationDTO);
    }

}
