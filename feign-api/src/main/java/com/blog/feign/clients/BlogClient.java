package com.blog.feign.clients;

import com.blog.feign.entity.CategoryVo;
import com.blog.feign.entity.NacosConfigEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = "blogservice")
public interface BlogClient {

    @RequestMapping("/category/getCategoryListTest")
    List<CategoryVo> getCategoryList();

    @RequestMapping("/category/getNacosConfig")
    NacosConfigEntity getNacosConfig();

}
