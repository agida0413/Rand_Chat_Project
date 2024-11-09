package rand.api.domain.member.service;

import org.springframework.http.ResponseEntity;
import rand.api.web.dto.common.ResponseDTO;
import rand.api.web.dto.member.request.EmailAuthCheckDTO;
import rand.api.web.dto.member.request.EmailAuthSendDTO;
import rand.api.web.dto.member.request.FindIdDTO;
import rand.api.web.dto.member.request.JoinDTO;
import rand.api.web.dto.member.response.ResFindIdDTO;

public interface MemberService {

    public ResponseEntity<ResponseDTO<Void>> emailAuthSend(EmailAuthSendDTO emailAuthDTO);

    public ResponseEntity<ResponseDTO<Void>> emailAuthCheck(EmailAuthCheckDTO emailAuthCheckDTO);

    public ResponseEntity<ResponseDTO<Void>> join(JoinDTO joinDTO);

    public ResponseEntity<ResponseDTO<ResFindIdDTO>> findId(FindIdDTO findIdDTO);

}
