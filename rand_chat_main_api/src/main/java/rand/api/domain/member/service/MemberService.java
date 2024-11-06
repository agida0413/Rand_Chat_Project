package rand.api.domain.member.service;

import org.springframework.http.ResponseEntity;
import rand.api.web.dto.common.ResponseDTO;
import rand.api.web.dto.member.request.EmailAuthDTO;

public interface MemberService {

    public ResponseEntity<ResponseDTO<Void>> emailDuplicateCheck(EmailAuthDTO emailAuthDTO);
}
