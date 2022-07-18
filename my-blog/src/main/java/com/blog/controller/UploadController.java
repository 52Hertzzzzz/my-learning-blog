package com.blog.controller;

import com.blog.service.UploadService;
import com.blog.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
public class UploadController {

    @Resource
    private UploadService uploadService;

    @PostMapping("/uploadImg")
    public Result<?> uploadImg(MultipartFile file){
        String url = uploadService.uploadImg(file);
        return Result.ok(url);
    }

}
