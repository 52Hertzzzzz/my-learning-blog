package com.essearch.controller;

import com.framework.utils.Result;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class ElasticSearchUserController {

    @Autowired
    private RestHighLevelClient client;

    @PostMapping("/queryUserDetail")
    public Result<?> queryUserDetail() {
    }

}
