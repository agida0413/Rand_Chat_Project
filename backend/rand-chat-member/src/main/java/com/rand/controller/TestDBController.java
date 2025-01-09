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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class TestDBController {

    private final MemberRepository memberRepository;
    private final ChatMsgRepository chatMsgRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MatchingRepository matchingRepository;

    @GetMapping
    @Transactional
    public String test(){
        int preUsrId;
        for(int i= 3; i<=10003; i++){


            Members members = new Members();
            members.setUsername("test"+i);
            members.setPassword("$2a$10$B.0TVquMQ.fSifE2LbJuSOJeQVts0Ho9R/O0sN2HXkYHrw6wwXrG6");
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

          if(i%2==0){
              preUsrId = i-1;

              Match match1 = new Match();
              matchingRepository.chatRoomMemCreate(match1);
              match1.setUsrId(String.valueOf(preUsrId));

              Match match2 = new Match();
              match2.setUsrId(String.valueOf(i));
              match2.setChatRoomId(match1.getChatRoomId());

              matchingRepository.chatRoomMemCreate(match1);
              matchingRepository.chatRoomMemCreate(match2);

              String chatRoomId =String.valueOf(match1.getChatRoomId()) ;
              int chatRommIdInt = Integer.parseInt(chatRoomId);

              for(int k = 0; k<200; k++){
                  ReqChatMsgSaveDTO reqChatMsgSaveDTO = new ReqChatMsgSaveDTO();
                  reqChatMsgSaveDTO.setChatRoomId(chatRommIdInt);
                  reqChatMsgSaveDTO.setMessage("테스트메시지"+i+k);
                  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                  String convertMsgCrDateMs = localDate.format(formatter);
                  reqChatMsgSaveDTO.setMsgCrDate(convertMsgCrDateMs);
                  LocalDateTime localDateTime = LocalDateTime.now();
                  reqChatMsgSaveDTO.setMsgCrDateMs(localDateTime);
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
