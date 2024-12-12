package com.rand.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDelDTO {
    //VALIDATAION_FAIL_PWD(400, "ERR-VALID-MEM-19", "현재 비밀번호는 공백일 수 없습니다."),
    @NotNull(message = "MemberDelDTO.비밀번호는 필수 입력 항목입니다.")
    @NotBlank(message = "MemberDelDTO.비밀번호는 공백일 수 없습니다.")
    private String password;
}
