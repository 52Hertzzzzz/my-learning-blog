package com.blog.controller;

import com.framework.service.UploadService;
import com.framework.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
public class UploadController {

    @Resource
    private UploadService uploadService;

    /***
     * 图片上传
     * @param file
     * @return
     */
    @PostMapping("/uploadImg")
    public Result<?> uploadImg(MultipartFile file){
        String url = uploadService.uploadImg(file);
        return Result.ok(url);
    }

//    @PostMapping("/uploadFile")
//    public Result<?> uploadFile(MultipartFile file){
//        uploadService.uploadFile(file);
//    }

}
