package com.rand.member.dto.request;

import com.rand.member.model.cons.MembersSex;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;

/*
회원가입

    이메일 인증 - > 이메일 : NOT NULL , 이메일 형식 유지 , 공백 포함 불가
    아이디 - > NOT NULL , 중복 체크 , 4 ~ 16자, 공백 포함 불가
    비밀번호 - > NOT NULL , 8~16자, 최소 하나의 문자, 특수문자, 숫자 포함, 공백 포함 불가
    휴대폰 번호 - > NOT NULL , 010,011등의 프론트엔드 제공 prefix 사용, 총 11자리, 숫자 형식, 공백 포함 불가
    이름 - > NOT NULL , 한국어만 허용, 공백 포함 불가
 */

@Getter
@Setter
public class JoinDTO {

    // 아이디: 4~16자, 공백 포함 불가
    @NotNull(message = "JoinDTO.아이디는 필수 입력 항목입니다.")
    @Pattern(regexp = "^[^\s][^\s]*$", message = "JoinDTO.아이디는 공백으로 시작하거나 공백을 포함할 수 없음")
    @NotBlank(message = "JoinDTO.아이디는 공백일 수 없습니다.")
    @Size(min = 8, max = 16, message = "JoinDTO.아이디는 8~16자여야 합니다.")
    private String username;

    // 비밀번호: 8~16자, 최소 하나의 문자, 특수문자, 숫자 포함, 공백 포함 불가
    @NotNull(message = "JoinDTO.비밀번호는 필수 입력 항목입니다.")
    @NotBlank(message = "JoinDTO.비밀번호는 공백일 수 없습니다.")
    @Size(min = 8, max = 16, message = "JoinDTO.비밀번호는 8~16자여야 합니다.")
    @Pattern(regexp = "^[^\s][^\s]*$", message = "JoinDTO.비밀번호는 공백으로 시작하거나 공백을 포함할 수 없음")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,16}$",
            message = "JoinDTO.비밀번호는 하나 이상의 문자, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    // 이메일: 이메일 형식, 공백 포함 불가
    @Email(message = "JoinDTO.유효한 이메일 형식이 아닙니다.")
    @NotNull(message = "JoinDTO.이메일은 필수 입력 항목입니다.")
    @NotBlank(message = "JoinDTO.이메일은 공백일 수 없습니다.")
    @Pattern(regexp = "^[^\s][^\s]*$", message = "JoinDTO.이메일은 공백으로 시작하거나 공백을 포함할 수 없음")
    private String email;



    // 닉네임: 한국어만 허용, 공백 포함 불가 3~15
    @NotNull(message = "JoinDTO.닉네임은 필수 입력 항목입니다.")
    @NotBlank(message = "JoinDTO.닉네임은 공백일 수 없습니다.")
    @Pattern(regexp = "^[^\s][^\s]*$", message = "JoinDTO.이름은 공백으로 시작하거나 공백을 포함할 수 없음")
    @Pattern(regexp = "^[가-힣]+$", message = "JoinDTO.이름은 한글만 입력할 수 있습니다.")
    @Size(min = 3, max = 15)
    private String nickName;


    private MembersSex sex;

    @Past(message = "JoinDTO.생일은 과거이여야 합니다")
    @NotNull(message = "JoinDTO.생일은 필수 입력 항목입니다.")
    private LocalDate birth;

}
