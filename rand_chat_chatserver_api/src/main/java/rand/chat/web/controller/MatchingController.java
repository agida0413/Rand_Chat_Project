package rand.chat.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rand.chat.domain.match.service.MatchService;
import rand.chat.web.dto.common.ResponseDTO;
import rand.chat.web.dto.match.request.MatchDTO;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/chat/match",produces = MediaType.APPLICATION_JSON_VALUE)
public class MatchingController {

    private final MatchService matchService;
    @GetMapping
    public ResponseEntity<ResponseDTO<Void>> matchUser(@Validated @ModelAttribute MatchDTO matchDTO){
        return  matchService.matchLogic(matchDTO);
    }

}
