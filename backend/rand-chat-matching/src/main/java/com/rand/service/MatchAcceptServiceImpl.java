package com.rand.service;

import com.rand.common.ResponseDTO;
import com.rand.config.constant.PubSubChannel;
import com.rand.config.var.RedisKey;
import com.rand.custom.SecurityContextGet;
import com.rand.exception.custom.BadRequestException;
import com.rand.jwt.JWTUtil;
import com.rand.match.dto.request.MatchAcceptDTO;
import com.rand.match.dto.response.ResMatchAcceptDTO;
import com.rand.match.model.AcceptState;
import com.rand.match.model.Match;
import com.rand.redis.InMemRepository;
import com.rand.redis.pubsub.NotificationService;
import com.rand.redis.pubsub.Publisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchAcceptServiceImpl implements MatchAcceptService {

    private final JWTUtil jwtUtil;
    private final InMemRepository inMemRepository;
    private final Publisher publisher;
    //채팅 매칭 수락 , 거절
    public ResponseEntity<ResponseDTO<ResMatchAcceptDTO>> matchAccept(MatchAcceptDTO matchAcceptDTO,String matchToken){

        //엔티티 변환
        Match match = new Match(matchAcceptDTO);

        //매칭 토큰이 없을 시
        if(matchToken ==null){
            throw new BadRequestException("ERR-SEC-06"); //비정상적인 접근입니다.
        }

        String usrId = String.valueOf(SecurityContextGet.getUsrId());
        //매칭토큰
        String connectionKey = matchToken+":"+usrId;

        boolean isFirstUsrId;

        //정합성 검사
        if(jwtUtil.getFirstUserId(matchToken).equals(usrId)){
            //현재 유저가 firstUsr임
            isFirstUsrId = true;
        }else if(jwtUtil.getSecondUserId(matchToken).equals(usrId)){
            //현재 유저가 secondUsr임
            isFirstUsrId = false;
        }
        else{
            //매칭토큰에 포함된 유저 2명과 현재 로그인유저 정보가 일치하지않음
            throw new BadRequestException("ERR-SEC-11"); //토큰과 로그인 정보가 일치하지 않음.
        }

        //매칭수락이 가능한 상태인지 확인
        Map<Object,Object> isExistMap =  inMemRepository.getHashValueEntry(RedisKey.MATCHING_ACCEPT_KEY+matchToken);

        //수락가능한 시간이 끝남
        if(isExistMap.isEmpty()){
            throw new BadRequestException("ERR-MATCH-01"); //매칭 수락,거절 가능시간이 종료되었습니다.
        }

        String key = RedisKey.MATCHING_ACCEPT_KEY+matchToken;
        //매칭된 첫번째 회원정보
        String firstUsrId = jwtUtil.getFirstUserId(matchToken);
        //매칭된 두번째 회원정보
        String secondUsrId = jwtUtil.getSecondUserId(matchToken);


        //응답 값
        ResMatchAcceptDTO resMatchAcceptDTO = new ResMatchAcceptDTO();

        //현재 회원이 첫번째 유저일 시
        if(isFirstUsrId){
            //두번째 유저의 현재 수락밸류
            String secondUsrVal = (String)inMemRepository.getHashValue(key , secondUsrId);

            //두번째 유저의 현재 수락밸류가 대기상태
            if(secondUsrVal.equals("WAIT")){

                if(match.isApproveChk()){
                    //현재유저가 수락
                    //SSE 연결포인트 - > 알람을 받아야함  - > 수락대기중
                    inMemRepository.hashSave(key,firstUsrId,"Y");
                    resMatchAcceptDTO.setAcceptState(AcceptState.WAITING);

                }else{
                    //현재유저가 거절
                    inMemRepository.hashSave(key,firstUsrId,"N");
                    resMatchAcceptDTO.setAcceptState(AcceptState.CLOSE);
                }
            } else if (secondUsrVal.equals("Y")) { //두번째 유저의 현재 수락밸류가 수락상태

                if(match.isApproveChk()){
                    //현재유저가 수락

                    inMemRepository.delete(key);
                    resMatchAcceptDTO.setAcceptState(AcceptState.SUCCESS);

                    String roomId = "";

                    //채팅방 생성 포인트

                    //SSE 전송 포인트
                    publisher.SendMatchingAcceptNotify(matchToken+":"+secondUsrId, PubSubChannel.MATCHING_ACCEPT_CHANNEL.toString(),AcceptState.SUCCESS,roomId);
                }else{
                    //현재유저가 거절
                    //SSE 전송 포인트
                    publisher.SendMatchingAcceptNotify(matchToken+":"+secondUsrId, PubSubChannel.MATCHING_ACCEPT_CHANNEL.toString(),AcceptState.REFUSED,null);
                    inMemRepository.delete(key);
                    resMatchAcceptDTO.setAcceptState(AcceptState.CLOSE);
                }
            } else if (secondUsrVal.equals("N")) { //두번째 유저의 현재 수락밸류가 거절상태
                inMemRepository.delete(key);
                resMatchAcceptDTO.setAcceptState(AcceptState.REFUSED);
            }
        } else if (!isFirstUsrId) {  //현재 회원이 두번째 유저

            //첫번째 유저의 현재 수락밸류
            String firstUsrVal = (String)inMemRepository.getHashValue(key , firstUsrId);

            //첫번째 유저의 현재 수락밸류가 대기상태
            if(firstUsrVal.equals("WAIT")){
                if(match.isApproveChk()){
                    //현재유저가 수락
                    //SSE 연결포인트 - > 알람을 받아야함  - > 수락대기중
                    inMemRepository.hashSave(key,secondUsrId,"Y");
                    resMatchAcceptDTO.setAcceptState(AcceptState.WAITING);

                }else{
                    //현재유저가 거절
                    inMemRepository.hashSave(key,secondUsrId,"N");
                    resMatchAcceptDTO.setAcceptState(AcceptState.CLOSE);
                }
            } else if (firstUsrVal.equals("Y")) { //첫번째 유저의 현재 수락밸류가 수락상태

                if(match.isApproveChk()){
                    //현재유저가 수락

                    inMemRepository.delete(key);
                    resMatchAcceptDTO.setAcceptState(AcceptState.SUCCESS);

                    String roomId = "";

                    //채팅방 생성 포인트

                    //SSE 전송 포인트
                    publisher.SendMatchingAcceptNotify(matchToken+":"+firstUsrId, PubSubChannel.MATCHING_ACCEPT_CHANNEL.toString(),AcceptState.SUCCESS,roomId);
                }else{
                    //현재유저가 거절
                    //SSE 전송 포인트
                    publisher.SendMatchingAcceptNotify(matchToken+":"+firstUsrId, PubSubChannel.MATCHING_ACCEPT_CHANNEL.toString(),AcceptState.REFUSED,null);
                    inMemRepository.delete(key);
                    resMatchAcceptDTO.setAcceptState(AcceptState.CLOSE);
                }
            } else if (firstUsrVal.equals("N")) { //첫번째 유저의 현재 수락밸류가 거절상태
                inMemRepository.delete(key);
                resMatchAcceptDTO.setAcceptState(AcceptState.REFUSED);
            }

        }

        resMatchAcceptDTO.setDescription();
        ResponseDTO<ResMatchAcceptDTO> responseDTO  = new ResponseDTO(resMatchAcceptDTO);
        return  ResponseEntity.ok(responseDTO);
    }
}
