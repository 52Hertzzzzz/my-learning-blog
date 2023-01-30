package com.admin.controller;

import com.admin.service.TagService;
import com.admin.entity.Tag;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.framework.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @DS("slave")
    @GetMapping("/tagList")
    public Result<?> tagList(){
        List<Tag> list = tagService.list();
        return Result.ok(list);
    }

    @DS("master")
    @PostMapping("/addTag")
    public Result<?> addTag(@RequestBody Tag tag) {
        boolean save = tagService.save(tag);
        if (save) {
            return Result.ok("保存成功");
        }

        return Result.error("保存失败");
    }

}
