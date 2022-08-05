package com.blog.controller;

import com.blog.entity.Tag;
import com.blog.service.TagService;
import com.blog.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("tagList")
    public Result<?> tagList(){

        List<Tag> list = tagService.list();
        return Result.ok(list);

    }

}
