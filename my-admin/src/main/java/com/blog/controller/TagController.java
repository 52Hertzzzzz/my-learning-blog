package com.blog.controller;

import com.blog.entity.Tag;
import com.blog.feign.clients.BlogClient;
import com.blog.feign.entity.CategoryVo;
import com.blog.feign.entity.NacosConfigEntity;
import com.blog.service.TagService;
import com.blog.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogClient blogClient;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/tagList")
    public Result<?> tagList(){
        List<Tag> list = tagService.list();
        return Result.ok(list);
    }

    @GetMapping("/testFeign")
    public Result<?> testFeign(){
        List<CategoryVo> categoryList = blogClient.getCategoryList();
        return Result.ok(categoryList);
    }

    @GetMapping("/testNacosConfig")
    public Result<?> testNacosConfig(){
        NacosConfigEntity nacosConfig = blogClient.getNacosConfig();
        return Result.ok(nacosConfig);
    }

    @GetMapping("/testRest")
    public Result<?> testRest(){
        String url = "http://blogservice/category/testFeign";
        String string1 = restTemplate.getForObject(url, String.class);
        return Result.ok(string1);
    }

}
