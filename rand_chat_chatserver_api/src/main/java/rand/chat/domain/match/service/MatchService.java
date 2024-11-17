package rand.chat.domain.match.service;

import org.springframework.http.ResponseEntity;
import rand.chat.web.dto.common.ResponseDTO;
import rand.chat.web.dto.match.request.MatchDTO;

public interface MatchService {

    public ResponseEntity<ResponseDTO<Void>> matchLogic(MatchDTO matchDTO);


}
