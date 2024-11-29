package com.rand.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rand.common.ResponseDTO;
import com.rand.member.dto.request.*;
import com.rand.member.dto.response.ResFindIdDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;


public interface MemberService {

    public ResponseEntity<ResponseDTO<Void>> emailAuthSend(EmailAuthSendDTO emailAuthDTO);

    public ResponseEntity<ResponseDTO<Void>> emailAuthCheck(EmailAuthCheckDTO emailAuthCheckDTO);

    public ResponseEntity<ResponseDTO<Void>> join(JoinDTO joinDTO);

    public ResponseEntity<ResponseDTO<ResFindIdDTO>> findId(FindIdDTO findIdDTO);

    public ResponseEntity<ResponseDTO<Void>> resetPwd(ResetPwdDTO pwdDTO);

    public ResponseEntity<ResponseDTO<Void>> emailUnlockAccountSend(UnlockAccountDTO unlockAccountDTO);
    public ResponseEntity<ResponseDTO<Void>> emailUnlockAccount(UnlockAccountChkDTO unlockAccountChkDTO);

    public ResponseEntity<ResponseDTO<Void>> memberCurLocationUpdate(CurLocationDTO curLocationDTO);

    public ResponseEntity<ResponseDTO<Void>> memberUpdatePwd(UpdatePwdDTO updatePwdDTO);
    public ResponseEntity<ResponseDTO<Void>> memberDel(String refreshToken,MemberDelDTO memberDelDTO);
}
