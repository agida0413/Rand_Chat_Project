package com.rand.service.wfx;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rand.chat.dto.request.ReqChatMsgSaveDTO;
import com.rand.chat.dto.request.ReqChatMsgUptDTO;
import com.rand.chat.dto.request.RoomValidDTO;
import com.rand.chat.model.*;
import com.rand.chat.repository.ChatMsgRepository;
import com.rand.chat.repository.ChatRoomRepository;
import com.rand.common.ErrorCode;
import com.rand.common.ResponseDTO;
import com.rand.common.service.CommonMemberService;
import com.rand.config.var.RedisKey;
import com.rand.custom.SecurityContextGet;
import com.rand.exception.custom.BadRequestException;
import com.rand.exception.custom.InternerServerException;
import com.rand.member.model.Members;
import com.rand.redis.InMemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

//WebClient를 통해 호출되는 api
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatWfxApiServiceImpl implements ChatWfxApiService {
    private final ChatRoomRepository chatRepository;
    private final CommonMemberService commonMemberService;
    private final InMemRepository inMemRepository;
    private final RedisTemplate<String,Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final ChatMsgRepository chatMsgRepository;

    //실제 채팅방에 참여중인지 검증하는 서비스
    @Override
    public Boolean isRealYourRoom(RoomValidDTO roomValidDTO) {
        RoomValid roomValid = chatRepository.isRealYourRoom(roomValidDTO);

        Boolean result =false;

        if(roomValid!=null && roomValid.getRoomState().equals(RoomState.ACTIVE.toString())){
            result = true;
        }

        return result;
    }
    //내가 아닌 상대방의 정보를 얻기위한 api
    @Override
    public Members getOpsMem(@PathVariable Integer chatRoomId) {
        //채팅방 참여자 리스트 조회
        List<Members> list = chatRepository.selectUsrIdInChatRoom(chatRoomId);
        int myUsrId = SecurityContextGet.getUsrId();

       Members memberInfo = new Members();

       //본인 필터링
        for(Members members : list){
            if(members.getUsrId()== myUsrId){
                continue;
            }
            else{
                memberInfo =commonMemberService.memberGetInfoMethod(members.getUsrId());
            }
        }

        return memberInfo;
    }

    // 채팅방에 입장플래그를 업데이트하는 api
    public ResponseEntity<ResponseDTO<Void>> asyncEnterRoomUpdateInfo(int usrId,Integer chatRoomId){
        inMemRepository.save(RedisKey.CUR_ENTER_ROOM_KEY+usrId,String.valueOf(chatRoomId));
        return ResponseEntity.ok().body(new ResponseDTO<>());
    }


    //채팅 메시지를 백그라운드에서 업데이트
    @Override
    @Transactional
    public ResponseEntity<ResponseDTO<Void>> asyncSaveChatMsgText(ReqChatMsgSaveDTO reqChatMsgSaveDTO){

        String usrId =String.valueOf(SecurityContextGet.getUsrId());
        String nickName = SecurityContextGet.getNickname();
        //채팅방 검증 (실제참여 ?)
        RoomValidDTO roomValidDTO = new RoomValidDTO();
        roomValidDTO.setChatRoomId(String.valueOf(reqChatMsgSaveDTO.getChatRoomId()));
        roomValidDTO.setUsrId(usrId);

        Boolean isReal=   isRealYourRoom(roomValidDTO);
        //채팅방검증
        if(isReal.equals(Boolean.FALSE)){
            throw new BadRequestException("ERR-CHAT-API-03");
        }

        //mysql엔티티변환
        ChatMessageSave chatMessageSave = new ChatMessageSave(usrId,reqChatMsgSaveDTO);
        //redis엔티티변환
        ChatMessageSaveRedis chatMessageSaveRedis = new ChatMessageSaveRedis(nickName,reqChatMsgSaveDTO);

        //  ZSetOperations
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
        //레디스 키
        String key = RedisKey.CHAT_MESSAGE_LIST_KEY + chatMessageSave.getChatRoomId();

        //복구를 위한 백업(삭제할 데이터)
        Set<ZSetOperations.TypedTuple<Object>> dataToRemove = null;
        //레디스에 저장할 수 있는 채팅메시지 총개수
        int maxSize = 100;

        //정합성을 위한 분산락
        boolean acquired = inMemRepository.lockCheck(RedisKey.CHAT_MESSAGE_LIST_LOCK_KEY+chatMessageSave.getChatRoomId(),RedisKey.MATCH_LOCK_TIMEOUT);

        if (acquired) {
            try {
                //레디스 저장 (메시지 생성일 - > 스코어 : 파라미터 , 실제저장값 : 변환 )

                //현재 사이즈
                long size = zSetOps.size(key);

                //사이즈가 100이상일 경우 FIFO 구조
                if (size >= maxSize) {

                    // 복구를 위한 백업 (스코어가 가장 낮은 데이터 1개만 조회)
                    dataToRemove = zSetOps.rangeWithScores(key, 0, 0); // 첫 번째 데이터만 조회

                    // 스코어가 가장 낮은 데이터 1개 삭제
                    zSetOps.removeRange(key, 0, 0); // 첫 번째 데이터만 삭제
                }
                //저장
                LocalDateTime msgCrDateMs = chatMessageSave.getMsgCrDateMs();

                double score = msgCrDateMs.toInstant(ZoneOffset.UTC).toEpochMilli();


                //최신메시지 저장
                String value = null;
                try {
                    value = objectMapper.writeValueAsString(chatMessageSaveRedis);
                    log.info("saved value = {}",value);
                } catch (JsonProcessingException e) {
                    //삭제한 데이터가 있다면 복구
                    if (dataToRemove != null) {
                        for (ZSetOperations.TypedTuple<Object> tuple : dataToRemove) {
                            zSetOps.add(key, tuple.getValue(), tuple.getScore());
                        }
                    }
                    throw new RuntimeException(e);
                }

                zSetOps.add(key, value, score);
            } finally {

                inMemRepository.delete(RedisKey.CHAT_MESSAGE_LIST_LOCK_KEY+chatMessageSave.getChatRoomId());
            }
        }
        else {
            //락획득 실패
            throw new InternerServerException("ERR-CMN-03");
        }

        //mysql저장
        try {
            chatMsgRepository.chatMsgSave(chatMessageSave);
        }
        catch (Exception ex){
            //락획득
            boolean acquiredSub = inMemRepository.lockCheck(RedisKey.CHAT_MESSAGE_LIST_LOCK_KEY+chatMessageSave.getChatRoomId(),RedisKey.MATCH_LOCK_TIMEOUT);
            if (acquiredSub) {
                try {
                    //최근 메시지 저장한것 삭제로 복구
                    zSetOps.removeRange(key, -1, -1); // 마지막 데이터 삭제

                    //삭제한 데이터가 있다면 복구
                    if (dataToRemove != null) {
                        for (ZSetOperations.TypedTuple<Object> tuple : dataToRemove) {
                            zSetOps.add(key, tuple.getValue(), tuple.getScore());
                        }
                    }
                }
                finally {
                    inMemRepository.delete(RedisKey.CHAT_MESSAGE_LIST_LOCK_KEY+chatMessageSave.getChatRoomId());
                }
            }else{
                throw new InternerServerException("ERR-CMN-03");
            }
            throw new RuntimeException(ex);
        }
        //오류발생시 레디스 메시지 삭제(dataToRemove 복구 , dataToSave 삭제)
        return  ResponseEntity.ok().body(new ResponseDTO<>());
    }
    
    
    //읽음여부 업데이트 
    @Override
    @Transactional
    public ResponseEntity<ResponseDTO<Void>> asyncSaveChatMsgIsReadUpt(ReqChatMsgUptDTO reqChatMsgUptDTO) {
        String usrId =String.valueOf(SecurityContextGet.getUsrId());
        //채팅방 검증 (실제참여 ?)
        RoomValidDTO roomValidDTO = new RoomValidDTO();
        roomValidDTO.setChatRoomId(String.valueOf(reqChatMsgUptDTO.getChatRoomId()));
        roomValidDTO.setUsrId(usrId);

        Boolean isReal=isRealYourRoom(roomValidDTO);

        //채팅방검증
        if(isReal.equals(Boolean.FALSE)){
            throw new BadRequestException("ERR-CHAT-API-03");
        }
        
        //도메인 변환
        ChatMessageUpdate chatMessageUpdate = new ChatMessageUpdate(reqChatMsgUptDTO);

        //읽음여부업데이트 리파지토리
        chatMsgRepository.chatMsgIsReadUpdate(chatMessageUpdate);

        //레디스 업데이트
        //정합성을 위한 분산락
        boolean acquired = inMemRepository.lockCheck(RedisKey.CHAT_MESSAGE_LIST_LOCK_KEY+reqChatMsgUptDTO.getChatRoomId(),RedisKey.MATCH_LOCK_TIMEOUT);

        //  ZSetOperations
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
        //레디스 키
        String key = RedisKey.CHAT_MESSAGE_LIST_KEY + reqChatMsgUptDTO.getChatRoomId();

        Set<ZSetOperations.TypedTuple<Object>> value = null;

        if(acquired){
            try{
                //스코어(읽은 시간)
                double score = reqChatMsgUptDTO.getReadDate().toInstant(ZoneOffset.UTC).toEpochMilli();
                // 읽은 시간보다 더 오래전 데이터 조회 (스코어 < score)
                value = zSetOps.reverseRangeByScoreWithScores(key, 0, score - 1);
                if (value != null) {

                    for (ZSetOperations.TypedTuple<Object> tuple : value) {

                        ChatMessageData chatMessageData = objectMapper.readValue((String) tuple.getValue(), ChatMessageData.class);
                        //업데이트 종료
                        if(chatMessageData.isRead() && chatMessageData.getNickName().equals(String.valueOf(reqChatMsgUptDTO.getNickName()))){
                            break;
                        }
                        //업데이트 요소 (업데이트할 메시지 + 읽음여부 x)
                        if(chatMessageData.getNickName().equals(String.valueOf(reqChatMsgUptDTO.getNickName()))){
                            chatMessageData.setRead(true);
                            String reSaveData = objectMapper.writeValueAsString(chatMessageData);
                            //기존 삭제
                            zSetOps.remove(key, tuple.getValue());
                            //읽음 true로 업데이트
                            zSetOps.add(key, reSaveData, tuple.getScore());
                        }

                    }
                }

            } catch (StreamReadException e) {
                throw new RuntimeException(e);
            } catch (DatabindException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                //락해제
                inMemRepository.delete(RedisKey.CHAT_MESSAGE_LIST_LOCK_KEY+reqChatMsgUptDTO.getChatRoomId());
            }
        }else{
            //락획득 실패
            throw new InternerServerException("ERR-CMN-03");
        }


        return ResponseEntity.ok().body(new ResponseDTO<>());
    }
}
