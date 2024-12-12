package com.rand.member.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResetPwdDTO {

    // 이메일: 이메일 형식, 공백 포함 불가
    @Email(message = "ResetPwdDTO.유효한 이메일 형식이 아닙니다.")
    @NotNull(message = "ResetPwdDTO.이메일은 필수 입력 항목입니다.")
    @NotBlank(message = "ResetPwdDTO.이메일은 공백일 수 없습니다.")
    @Pattern(regexp = "^[^\s][^\s]*$", message = "ResetPwdDTO.이메일은 공백으로 시작하거나 공백을 포함할 수 없음")
    private String email;


    @NotNull(message = "ResetPwdDTO.이름은 필수 입력 항목입니다.")
    @NotBlank(message = "ResetPwdDTO.이름은 공백일 수 없습니다.")
    @Pattern(regexp = "^[^\s][^\s]*$", message = "ResetPwdDTO.이름은 공백으로 시작하거나 공백을 포함할 수 없음")
    private String name;

    // 아이디: 4~16자, 공백 포함 불가
    @NotNull(message = "ResetPwdDTO.아이디는 필수 입력 항목입니다.")
    @Pattern(regexp = "^[^\s][^\s]*$", message = "ResetPwdDTO.아이디는 공백으로 시작하거나 공백을 포함할 수 없음")
    @NotBlank(message = "ResetPwdDTO.아이디는 공백일 수 없습니다.")
    private String username;
}
