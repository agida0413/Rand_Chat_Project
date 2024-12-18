package com.rand.member.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindIdDTO {
    // 이메일: 이메일 형식, 공백 포함 불가
    @Email(message = "FindIdDTO.유효한 이메일 형식이 아닙니다.")
    @NotNull(message = "FindIdDTO.이메일은 필수 입력 항목입니다.")
    @NotBlank(message = "FindIdDTO.이메일은 공백일 수 없습니다.")
    @Pattern(regexp = "^[^\s][^\s]*$", message = "FindIdDTO.이메일은 공백으로 시작하거나 공백을 포함할 수 없음")
    private String email;


    @NotNull(message = "FindIdDTO.이름은 필수 입력 항목입니다.")
    @NotBlank(message = "FindIdDTO.이름은 공백일 수 없습니다.")
    @Pattern(regexp = "^[^\s][^\s]*$", message = "FindIdDTO.이름은 공백으로 시작하거나 공백을 포함할 수 없음")
    @Pattern(regexp = "^[가-힣]{2,8}$", message = "FindIdDTO.이름은 2~8자의 한글만 입력할 수 있습니다.")
    private String name;
}
