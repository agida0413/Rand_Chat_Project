package com.rand.member.dto.response;

import com.rand.member.model.Members;
import com.rand.member.model.cons.MembersSex;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ResMemInfoDTO {

    private String email;
    private String username;
    private String nickname;
    private String name;
    private String profile_img;
    private String sex;
    private LocalDate birth;
    private int manAge; //만나이

    public ResMemInfoDTO(Members members){
        this.email=members.getEmail();
        this.username=members.getUsername();
        this.nickname=members.getNickName();
        this.name = members.getName();
        this.profile_img = members.getProfileImg();

        if(members.getSex().equals(MembersSex.MAN)){
            this.sex= "남자";
        }
        else if(members.getSex().equals(MembersSex.FEMAIL)){
            this.sex = "여자";
        }

        this.birth =members.getBirth();

        this.manAge =calculateKoreanAge(members.getBirth());


    }

    private static int calculateKoreanAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        int age = currentDate.getYear() - birthDate.getYear();

        // 생일이 지나지 않았다면 1살을 빼야 한다
        if (currentDate.getMonthValue() < birthDate.getMonthValue() ||
                (currentDate.getMonthValue() == birthDate.getMonthValue() && currentDate.getDayOfMonth() < birthDate.getDayOfMonth())) {
            age--;
        }

        // 한국 나이는 1을 더해줘야 하므로
        return age + 1;
    }
}
