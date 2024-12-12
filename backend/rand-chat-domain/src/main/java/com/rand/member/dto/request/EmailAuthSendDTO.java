package com.rand.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmailAuthSendDTO {
    // 이메일: 이메일 형식, 공백 포함 불가
    @Email(message = "EmailAuthSendDTO.유효한 이메일 형식이 아닙니다.")
    @NotNull(message = "EmailAuthSendDTO.이메일은 필수 입력 항목입니다.")
    @NotBlank(message = "EmailAuthSendDTO.이메일은 공백일 수 없습니다.")
    @Pattern(regexp = "^[^\s][^\s]*$", message = "EmailAuthDTO.이메일은 공백으로 시작하거나 공백을 포함할 수 없음")
    private String email;
}
