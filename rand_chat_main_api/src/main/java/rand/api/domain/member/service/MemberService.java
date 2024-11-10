package rand.api.domain.member.service;

import org.springframework.http.ResponseEntity;
import rand.api.web.dto.common.ResponseDTO;
import rand.api.web.dto.member.request.*;
import rand.api.web.dto.member.response.ResFindIdDTO;

public interface MemberService {

    public ResponseEntity<ResponseDTO<Void>> emailAuthSend(EmailAuthSendDTO emailAuthDTO);

    public ResponseEntity<ResponseDTO<Void>> emailAuthCheck(EmailAuthCheckDTO emailAuthCheckDTO);

    public ResponseEntity<ResponseDTO<Void>> join(JoinDTO joinDTO);

    public ResponseEntity<ResponseDTO<ResFindIdDTO>> findId(FindIdDTO findIdDTO);

    public ResponseEntity<ResponseDTO<Void>> resetPwd(ResetPwdDTO pwdDTO);

    public ResponseEntity<ResponseDTO<Void>> emailUnlockAccountSend(UnlockAccountDTO unlockAccountDTO);
    public ResponseEntity<ResponseDTO<Void>> emailUnlockAccount(UnlockAccountChkDTO unlockAccountChkDTO);

}
