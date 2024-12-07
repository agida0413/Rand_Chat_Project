package com.rand.redis.pubsub;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rand.common.ErrorCode;
import com.rand.common.ResponseDTO;
import com.rand.common.ResponseErr;
import com.rand.config.constant.PubSubChannel;
import com.rand.config.constant.SSETYPE;
import com.rand.config.var.RedisKey;
import com.rand.match.dto.response.ResMatchAcceptDTO;
import com.rand.match.dto.response.ResMatchResultDTO;
import com.rand.match.model.AcceptState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubsCriber implements MessageListener {


    private final NotificationService connectionService;




    // Redis 메시지 수신 처리
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try{
            String payload = new String(message.getBody());
            Map<String, String> data = null;

            data = parsePayload(payload);
            //필수 값
            String userId = data.get("userId");
            String channel = data.get("channel");


            String serverInstanceId = "";
            // 현재 서버에 연결된 클라이언트라면 메시지 전송

            if(channel.equals(PubSubChannel.MATCHING_CHANNEL.toString())){
                //매칭 채널
                serverInstanceId = connectionService.getServerInstanceForUser(userId,PubSubChannel.MATCHING_CHANNEL.toString(), RedisKey.SSE_MATCHING_CONNECTION_KEY);
            }
            else if (channel.equals(PubSubChannel.MATCHING_ACCEPT_CHANNEL.toString())){
                    serverInstanceId = connectionService.getServerInstanceForUser(userId,PubSubChannel.MATCHING_ACCEPT_CHANNEL.toString(), RedisKey.SSE_MATCHING_ACCEPT_CONNECTION_KEY);
            }



            if (isCurrentInstance(serverInstanceId)) {


                //분기
                if(channel.equals(PubSubChannel.MATCHING_CHANNEL.toString())){
                    //매칭채널
                    String nickname = data.get("nickname");
                    String profileImg = data.get("profileImg");
                    String sex = data.get("sex");
                    String type= data.get("type");
                    String distance = data.get("distance");
                    String matchingAcceptKey = data.get("matchingAcceptKey");
                    matchingResultSendToClient(userId, nickname,profileImg,sex,type,distance,matchingAcceptKey);


                }else if (channel.equals(PubSubChannel.MATCHING_ACCEPT_CHANNEL.toString())){
                    //매칭 수락 채널
                    String state = data.get("state");
                    String roomId = data.get("roomId");
                    matchingAcceptSendToClient(userId,state,roomId);
                }

                //특정 EMITTER 가져오기
                SseEmitter emitter = SseConnectionRegistry.getEmitter(userId, channel);
                emitter.complete(); // 연결 종료
            }

        }
        catch (Exception e){
            throw new RuntimeException(e);
        }



    }

    private Map<String, String> parsePayload(String payload) throws JsonProcessingException {
        // JSON 문자열을 Map으로 변환 (간단한 파서 사용)
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(payload, new TypeReference<>() {});
    }

    private boolean isCurrentInstance(String serverInstanceId) {
        // 현재 서버 인스턴스와 비교 (로드밸런싱 환경에서 인스턴스 ID 비교)
        return getCurrentInstanceId().equals(serverInstanceId);
    }

    //매칭 결과 전송
    private void matchingResultSendToClient(String userId, String nickname,String profileImg,String sex,String type,String distance,String matchingAcceptKey) {
        // SSE 연결된 클라이언트에게 메시지 전송
        SseEmitter emitter = SseConnectionRegistry.getEmitter(userId,PubSubChannel.MATCHING_CHANNEL.toString());
        if (emitter != null) {
            try {
                if(type.equals(SSETYPE.MATCHINGCOMPLETE.toString())){
                    ResMatchResultDTO resMatchResultDTO = new ResMatchResultDTO();
                    resMatchResultDTO.setNickname(nickname);
                    resMatchResultDTO.setProfileImg(profileImg);
                    resMatchResultDTO.setSex(sex);
                    resMatchResultDTO.setDistance(distance);
                    resMatchResultDTO.setMatchAcptToken(matchingAcceptKey);

                    ResponseDTO<ResMatchResultDTO> responseDTO = new ResponseDTO(resMatchResultDTO);
                    emitter.send(SseEmitter.event().name(PubSubChannel.MATCHING_CHANNEL.toString()).data(responseDTO));

                }
                else if(type.equals(SSETYPE.MATCHINGTIMEOUT.toString())){
                    ResponseErr responseErr = new ResponseErr(ErrorCode.COMMON_SSE_MATCH_1MIN_TIME_OUT);
                    emitter.send(SseEmitter.event().name(PubSubChannel.MATCHING_CHANNEL.toString()).data(responseErr));

                }


            } catch (Exception e) {
                // 전송 실패 시 처리
                SseConnectionRegistry.removeEmitter(userId,PubSubChannel.MATCHING_CHANNEL.toString());
                log.info("test result= fail");
            }
        }
    }

    private void matchingAcceptSendToClient(String userId,String state,String roomId) {
        // SSE 연결된 클라이언트에게 메시지 전송
        SseEmitter emitter = SseConnectionRegistry.getEmitter(userId,PubSubChannel.MATCHING_ACCEPT_CHANNEL.toString());
        log.info("emmiter1={}",emitter);
        if (emitter != null) {
            try {

                ResMatchAcceptDTO resMatchAcceptDTO = new ResMatchAcceptDTO();
                resMatchAcceptDTO.setRoomId(roomId);
                if(state.equals(AcceptState.CLOSE.toString())){
                    resMatchAcceptDTO.setAcceptState(AcceptState.CLOSE);

                }
                if(state.equals(AcceptState.WAITING.toString())){
                    resMatchAcceptDTO.setAcceptState(AcceptState.WAITING);
                }
                if(state.equals(AcceptState.SUCCESS.toString())){
                    resMatchAcceptDTO.setAcceptState(AcceptState.SUCCESS);
                }
                if(state.equals(AcceptState.REFUSED.toString())){
                    resMatchAcceptDTO.setAcceptState(AcceptState.REFUSED);
                }
                resMatchAcceptDTO.setDescription();

                ResponseDTO responseDTO = new ResponseDTO(resMatchAcceptDTO);
                    emitter.send(SseEmitter.event().name(PubSubChannel.MATCHING_ACCEPT_CHANNEL.toString()).data(responseDTO));


            } catch (Exception e) {
                // 전송 실패 시 처리
                SseConnectionRegistry.removeEmitter(userId,PubSubChannel.MATCHING_ACCEPT_CHANNEL.toString());
                log.info("test result= fail");
            }
        }
    }




    private String getCurrentInstanceId() {
        // 서버 인스턴스 ID 반환 (필요시 환경변수 또는 설정값 사용)
        log.info("serverId={}",System.getenv("INSTANCE_ID"));
        return System.getenv("INSTANCE_ID");
    }


}
