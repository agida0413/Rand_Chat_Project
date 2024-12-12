package com.rand.member.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateProfileImgDTO {

    private MultipartFile uptProfileImg;

}
