package com.framework.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    String uploadImg(MultipartFile file);

//    void uploadFile(MultipartFile file);
}
