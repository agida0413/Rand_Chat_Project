package com.rand.service.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rand.chat.dto.request.ReqImgSave;
import com.rand.chat.dto.response.ResChatMsg;
import com.rand.chat.dto.response.ResImgSave;
import com.rand.chat.model.ChatMessage;
import com.rand.chat.model.ChatMessageData;
import com.rand.chat.model.ChatMsgList;
import com.rand.chat.repository.ChatMsgRepository;
import com.rand.common.ErrorCode;
import com.rand.common.ResponseDTO;
import com.rand.config.var.RedisKey;
import com.rand.exception.custom.BadRequestException;
import com.rand.exception.custom.InternerServerException;
import com.rand.service.file.FileService;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMsgServiceImpl implements ChatMsgService{
    private final ChatMsgRepository chatMsgRepository;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String,Object> redisTemplate;
    private final FileService fileService;
    private static final int ROW_SIZE=100;

    //채팅 메시지 정보 FROM REDIS 1페이지
    @Override
    public ResponseEntity<ResponseDTO<List<ResChatMsg>>> selectChatMsgListInMemory(Integer chatRoomId) {
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();

        String key = RedisKey.CHAT_MESSAGE_LIST_KEY + chatRoomId;

        //캐시 히트 시도
        Set<ZSetOperations.TypedTuple<Object>> chatData = zSetOps.reverseRangeWithScores(key, 0, -1); // -1은 모든 데이터를 의미

        List <ResChatMsg> list = new ArrayList<>();
        //리스트가 존재하면

        if(!chatData.isEmpty()){
            log.info("캐시히트");
            //값 변환
            list = chatData.stream()
                    .map(tuple -> {
                        try {
                            // JSON 문자열을 ResChatMsg 객체로 변환
                            return objectMapper.readValue(tuple.getValue().toString(), ResChatMsg.class);
                        } catch (Exception e) {
                            throw new BadRequestException("ERR-CMN-01");
                        }
                    })
                    .collect(Collectors.toList()); // Collect to List
        }else{
            log.info("캐시미스");
            //캐시 값이 없다면
            //캐시미스 시 1페이지 RDBMS에서 갖고옴
            ChatMsgList chatMsgList = new ChatMsgList(0,ROW_SIZE,chatRoomId);
            list = chatMsgRepository.selectChatMsgList(chatMsgList);

            //결과가 0 임
            if(list ==null || list.isEmpty()  ){
                throw new BadRequestException("ERR-CHAT-API-04");
            }

            //캐시 WRITE
            list.forEach(resChatMsg -> {
                try {
                    //String 의 데이터를 LoclaDateTime으로 바꿈 - > 스코어 변환
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
                    LocalDateTime localDateTime = LocalDateTime.parse(resChatMsg.getMsgCrDateMs(), formatter);
                    double score = localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();

                    String value = objectMapper.writeValueAsString(resChatMsg); // ResChatMsg를 JSON 문자열로 변환
                    zSetOps.add(key, value, score); // Redis ZSet에 추가
                } catch (Exception e) {
                    throw new InternerServerException("ERR-CMN-01");
                }
            });
        }


        //RES DTO 변환
        return ResponseEntity.ok()
                .body(new ResponseDTO<List<ResChatMsg>>(list));
    }
    //채팅 메시지 정보 FROM RDBMS
    @Override
    public ResponseEntity<ResponseDTO<List<ResChatMsg>>> selectChatMsgListRDBMS(Integer chatRoomId, Integer page) {
        //OFFSET
        int offSet = (page - 1) * ROW_SIZE;
        //모델 변환
        ChatMsgList chatMsgList = new ChatMsgList(offSet,ROW_SIZE,chatRoomId);
        //메시지 리스트 RDBMS에서 갖고옴
        List<ResChatMsg> list = chatMsgRepository.selectChatMsgList(chatMsgList);

       //결과없음
        if(list ==null || list.isEmpty()  ){
            throw new BadRequestException("ERR-CHAT-API-04");
        }

        return ResponseEntity
                .ok()
                .body(new ResponseDTO<List<ResChatMsg>>(list) );
    }
    //파일업로드
    @Override
    public ResponseEntity<ResponseDTO<ResImgSave>> getSendImgUrl(ReqImgSave reqImgSave) {
        String saveUrl="";
        try {
           saveUrl= fileService.upload(reqImgSave.getImg());
        }
        catch (Exception e){
            throw new BadRequestException("ERR-CHAT-API-06");
        }

        ResImgSave resImgSave = new ResImgSave();
        resImgSave.setImgUrl(saveUrl);
        return ResponseEntity
                .ok()
                .body(new ResponseDTO<>(resImgSave));
    }
}
