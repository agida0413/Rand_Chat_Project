package com.rand.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    public String upload(MultipartFile image);
    public void deleteImage(String imageAddress);
}
