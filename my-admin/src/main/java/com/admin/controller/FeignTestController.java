package com.admin.controller;

import com.admin.entity.TestPerson;
import com.blog.feign.clients.BlogClient;
import com.blog.feign.clients.RabbitMQClient;
import com.blog.feign.entity.CategoryVo;
import com.blog.feign.entity.NacosConfigEntity;
import com.framework.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/feignTest")
public class FeignTestController {

    @Autowired
    private BlogClient blogClient;

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Autowired
    private RestTemplate restTemplate;

    @Resource
    //@Qualifier("testPerson1")
    private TestPerson testPerson2;

    @GetMapping("/test1")
    public Result<?> test1() {
        List<CategoryVo> categoryList = blogClient.getCategoryList();
        return Result.ok(categoryList);
    }

    @GetMapping("/test2")
    public Result<?> test2() {
        NacosConfigEntity nacosConfig = blogClient.getNacosConfig();
        return Result.ok(nacosConfig);
    }

    @GetMapping("/test3")
    public Result<?> test3() {
        String url = "http://blogservice/category/testFeign";
        String string1 = restTemplate.getForObject(url, String.class);
        return Result.ok(string1);
    }

    @PostMapping("/test4")
    public Result<?> test4(@RequestBody String string) {
        Result<?> result = rabbitMQClient.send1(string);
        return result;
    }

    @GetMapping("/test5")
    public Result<?> test5() {
        String s = testPerson2.toString();
        return Result.ok(s);
    }

}
