package com.rand.chat.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
public class ReqChatMsgUptDTO {
    @Min(1)
    private int usrId;
    @NotNull
    private Integer chatRoomId;
    @NotNull
    @FutureOrPresent()
    private LocalDateTime readDate;
    @NotNull
    private String nickName;
    //api서버에서 받는 생성자
    public ReqChatMsgUptDTO(){

    }
    //웹소켓 서버에서 객체생성
    public ReqChatMsgUptDTO(int usrId,String nickName,Integer chatRoomId){
        this.nickName = nickName;
    this.usrId  = usrId;
    this.chatRoomId = chatRoomId;
    this.readDate = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Seoul"));
    }
}
