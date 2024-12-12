package com.rand.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePwdDTO {
    @NotNull(message = "UpdatePwdDTO.비밀번호는 필수 입력 항목입니다.")
    @NotBlank(message = "UpdatePwdDTO.비밀번호는 공백일 수 없습니다.")
    private String password;

    @NotNull(message = "UpdatePwdDTO.비밀번호는 필수 입력 항목입니다.")
    @NotBlank(message = "UpdatePwdDTO.비밀번호는 공백일 수 없습니다.")
    @Size(min = 8, max = 16, message = "UpdatePwdDTO.비밀번호는 8~16자여야 합니다.")
    @Pattern(regexp = "^[^\s][^\s]*$", message = "UpdatePwdDTO.비밀번호는 공백으로 시작하거나 공백을 포함할 수 없음")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,16}$",
            message = "UpdatePwdDTO.비밀번호는 하나 이상의 문자, 숫자, 특수문자를 포함해야 합니다.")
    private String newPassword;
}
