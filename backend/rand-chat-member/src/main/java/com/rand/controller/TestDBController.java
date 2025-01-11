package com.rand.controller;

import com.rand.chat.dto.request.ReqChatMsgSaveDTO;
import com.rand.chat.model.ChatMessageSave;
import com.rand.chat.model.ChatType;
import com.rand.chat.repository.ChatMsgRepository;
import com.rand.chat.repository.ChatRoomRepository;
import com.rand.match.model.Match;
import com.rand.match.repository.MatchingRepository;
import com.rand.member.model.Members;
import com.rand.member.model.cons.MembersSex;
import com.rand.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class TestDBController {

    private final MemberRepository memberRepository;
    private final ChatMsgRepository chatMsgRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MatchingRepository matchingRepository;

    @GetMapping("/1")
    @Transactional
    public String test(){
        int preUsrId;
        for(int i= 3; i<=10003; i++){


            Members members = new Members();
            members.setUsername("test"+i);
            members.setPassword("$10$jGojAoDqOrIkLHKxXkdP1O//x.Vcl3VC/b2eFo4kk8YTyyjB9ttTa");
            members.setEmail("test"+i+"@naver.com");
            members.setNickName("테스트닉네임"+i);
            members.setName("테스트이름"+i);
            if(i%2==0){
                members.setSex(MembersSex.FEMAIL);
            }else{

                members.setSex(MembersSex.MAN);
            }
            LocalDate localDate = LocalDate.now();
            members.setBirth(localDate);

            memberRepository.join(members);



        }

        return "OK";
    }

    @GetMapping("/2")
    @Transactional
    public String test2(){
        int preUsrId;
        for(int i= 3; i<=501; i++){

            if(i%2==0){
                preUsrId = i-1;

                Match match1 = new Match();
                matchingRepository.chatRoomCreate(match1);
                match1.setUsrId(String.valueOf(preUsrId));

                Match match2 = new Match();
                match2.setUsrId(String.valueOf(i));
                match2.setChatRoomId(match1.getChatRoomId());
                log.info("tr={}",match1.getUsrId());
                log.info("tr={}",match2.getUsrId());
                matchingRepository.chatRoomMemCreate(match1);
                matchingRepository.chatRoomMemCreate(match2);

                String chatRoomId =String.valueOf(match1.getChatRoomId()) ;
                int chatRommIdInt = Integer.parseInt(chatRoomId);

                for(int k = 0; k<110; k++){
                    ReqChatMsgSaveDTO reqChatMsgSaveDTO = new ReqChatMsgSaveDTO();
                    reqChatMsgSaveDTO.setChatRoomId(chatRommIdInt);
                    reqChatMsgSaveDTO.setMessage("테스트메시지"+i+k);
                    LocalDateTime localDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    String convertMsgCrDateMs = localDateTime.format(formatter);
                    reqChatMsgSaveDTO.setMsgCrDate(convertMsgCrDateMs);
                    reqChatMsgSaveDTO.setMsgCrDateMs(localDateTime);
                    reqChatMsgSaveDTO.setMsgCrDate(convertMsgCrDateMs);
                    LocalDateTime localDateTime1 = LocalDateTime.now();
                    reqChatMsgSaveDTO.setMsgCrDateMs(localDateTime1);
                    reqChatMsgSaveDTO.setRead(false);
                    reqChatMsgSaveDTO.setChatType(ChatType.TEXT);
                    if(k % 2 ==0){
                        ChatMessageSave chatMessageSave = new ChatMessageSave(String.valueOf(preUsrId),reqChatMsgSaveDTO);
                        chatMsgRepository.chatMsgSave(chatMessageSave);
                    }else{
                        ChatMessageSave chatMessageSave = new ChatMessageSave(String.valueOf(i),reqChatMsgSaveDTO);
                        chatMsgRepository.chatMsgSave(chatMessageSave);
                    }


                }
            }

        }

        return "OK";
    }

    @GetMapping("/3")
    @Transactional
    public String test3(){
        int preUsrId;
        for(int i= 502; i<=1001; i++){

            if(i%2==0){
                preUsrId = i-1;

                Match match1 = new Match();
                matchingRepository.chatRoomCreate(match1);
                match1.setUsrId(String.valueOf(preUsrId));

                Match match2 = new Match();
                match2.setUsrId(String.valueOf(i));
                match2.setChatRoomId(match1.getChatRoomId());
                log.info("tr={}",match1.getUsrId());
                log.info("tr={}",match2.getUsrId());
                matchingRepository.chatRoomMemCreate(match1);
                matchingRepository.chatRoomMemCreate(match2);

                String chatRoomId =String.valueOf(match1.getChatRoomId()) ;
                int chatRommIdInt = Integer.parseInt(chatRoomId);

                for(int k = 0; k<110; k++){
                    ReqChatMsgSaveDTO reqChatMsgSaveDTO = new ReqChatMsgSaveDTO();
                    reqChatMsgSaveDTO.setChatRoomId(chatRommIdInt);
                    reqChatMsgSaveDTO.setMessage("테스트메시지"+i+k);
                    LocalDateTime localDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    String convertMsgCrDateMs = localDateTime.format(formatter);
                    reqChatMsgSaveDTO.setMsgCrDate(convertMsgCrDateMs);
                    reqChatMsgSaveDTO.setMsgCrDateMs(localDateTime);
                    reqChatMsgSaveDTO.setMsgCrDate(convertMsgCrDateMs);
                    LocalDateTime localDateTime1 = LocalDateTime.now();
                    reqChatMsgSaveDTO.setMsgCrDateMs(localDateTime1);
                    reqChatMsgSaveDTO.setRead(false);
                    reqChatMsgSaveDTO.setChatType(ChatType.TEXT);
                    if(k % 2 ==0){
                        ChatMessageSave chatMessageSave = new ChatMessageSave(String.valueOf(preUsrId),reqChatMsgSaveDTO);
                        chatMsgRepository.chatMsgSave(chatMessageSave);
                    }else{
                        ChatMessageSave chatMessageSave = new ChatMessageSave(String.valueOf(i),reqChatMsgSaveDTO);
                        chatMsgRepository.chatMsgSave(chatMessageSave);
                    }


                }
            }

        }

        return "OK";
    }

    @GetMapping("/4")
    @Transactional
    public String test4(){
        int preUsrId;
        for(int i= 1002; i<=1501; i++){

            if(i%2==0){
                preUsrId = i-1;

                Match match1 = new Match();
                matchingRepository.chatRoomCreate(match1);
                match1.setUsrId(String.valueOf(preUsrId));

                Match match2 = new Match();
                match2.setUsrId(String.valueOf(i));
                match2.setChatRoomId(match1.getChatRoomId());
                log.info("tr={}",match1.getUsrId());
                log.info("tr={}",match2.getUsrId());
                matchingRepository.chatRoomMemCreate(match1);
                matchingRepository.chatRoomMemCreate(match2);

                String chatRoomId =String.valueOf(match1.getChatRoomId()) ;
                int chatRommIdInt = Integer.parseInt(chatRoomId);

                for(int k = 0; k<110; k++){
                    ReqChatMsgSaveDTO reqChatMsgSaveDTO = new ReqChatMsgSaveDTO();
                    reqChatMsgSaveDTO.setChatRoomId(chatRommIdInt);
                    reqChatMsgSaveDTO.setMessage("테스트메시지"+i+k);
                    LocalDateTime localDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    String convertMsgCrDateMs = localDateTime.format(formatter);
                    reqChatMsgSaveDTO.setMsgCrDate(convertMsgCrDateMs);
                    reqChatMsgSaveDTO.setMsgCrDateMs(localDateTime);
                    reqChatMsgSaveDTO.setMsgCrDate(convertMsgCrDateMs);
                    LocalDateTime localDateTime1 = LocalDateTime.now();
                    reqChatMsgSaveDTO.setMsgCrDateMs(localDateTime1);
                    reqChatMsgSaveDTO.setRead(false);
                    reqChatMsgSaveDTO.setChatType(ChatType.TEXT);
                    if(k % 2 ==0){
                        ChatMessageSave chatMessageSave = new ChatMessageSave(String.valueOf(preUsrId),reqChatMsgSaveDTO);
                        chatMsgRepository.chatMsgSave(chatMessageSave);
                    }else{
                        ChatMessageSave chatMessageSave = new ChatMessageSave(String.valueOf(i),reqChatMsgSaveDTO);
                        chatMsgRepository.chatMsgSave(chatMessageSave);
                    }


                }
            }

        }

        return "OK";
    }

    @GetMapping("/5")
    @Transactional
    public String test5(){
        int preUsrId;
        for(int i= 1501; i<=4001; i++){

            if(i%2==0){
                preUsrId = i-1;

                Match match1 = new Match();
                matchingRepository.chatRoomCreate(match1);
                match1.setUsrId(String.valueOf(preUsrId));

                Match match2 = new Match();
                match2.setUsrId(String.valueOf(i));
                match2.setChatRoomId(match1.getChatRoomId());
                log.info("tr={}",match1.getUsrId());
                log.info("tr={}",match2.getUsrId());
                matchingRepository.chatRoomMemCreate(match1);
                matchingRepository.chatRoomMemCreate(match2);

                String chatRoomId =String.valueOf(match1.getChatRoomId()) ;
                int chatRommIdInt = Integer.parseInt(chatRoomId);

                for(int k = 0; k<110; k++){
                    ReqChatMsgSaveDTO reqChatMsgSaveDTO = new ReqChatMsgSaveDTO();
                    reqChatMsgSaveDTO.setChatRoomId(chatRommIdInt);
                    reqChatMsgSaveDTO.setMessage("테스트메시지"+i+k);
                    LocalDateTime localDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    String convertMsgCrDateMs = localDateTime.format(formatter);
                    reqChatMsgSaveDTO.setMsgCrDate(convertMsgCrDateMs);
                    reqChatMsgSaveDTO.setMsgCrDateMs(localDateTime);
                    reqChatMsgSaveDTO.setMsgCrDate(convertMsgCrDateMs);
                    LocalDateTime localDateTime1 = LocalDateTime.now();
                    reqChatMsgSaveDTO.setMsgCrDateMs(localDateTime1);
                    reqChatMsgSaveDTO.setRead(false);
                    reqChatMsgSaveDTO.setChatType(ChatType.TEXT);
                    if(k % 2 ==0){
                        ChatMessageSave chatMessageSave = new ChatMessageSave(String.valueOf(preUsrId),reqChatMsgSaveDTO);
                        chatMsgRepository.chatMsgSave(chatMessageSave);
                    }else{
                        ChatMessageSave chatMessageSave = new ChatMessageSave(String.valueOf(i),reqChatMsgSaveDTO);
                        chatMsgRepository.chatMsgSave(chatMessageSave);
                    }


                }
            }

        }

        return "OK";
    }
}
