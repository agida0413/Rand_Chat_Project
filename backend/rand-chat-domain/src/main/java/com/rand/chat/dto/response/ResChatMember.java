package com.rand.chat.dto.response;

import com.rand.member.dto.response.ResMemInfoDTO;
import com.rand.member.model.cons.MembersSex;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResChatMember {
private String nickName;
private String sex;
private int manAge;
private String profileImg;
private boolean itsMeFlag;
private Double betweenDistance;

    public ResChatMember(ResMemInfoDTO dto,boolean flag,Double betweenDistance){
        this.nickName = dto.getNickname();
        this.sex = dto.getSex();
        this.manAge =dto.getManAge();
        this.profileImg =dto.getProfile_img();
        this.itsMeFlag = flag;
        this.betweenDistance  = betweenDistance;
    }
}
