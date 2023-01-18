package com.producer.controller;

import com.alibaba.fastjson.JSONObject;
import com.blog.entity.MessageEntity;
import com.blog.enums.AppHttpCodeEnum;
import com.blog.exception.SystemException;
import com.blog.utils.Result;
import com.producer.serivce.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class ProducerController {

    @Autowired
    private ProducerService producerService;

    //发送消息
    @PostMapping("/send1")
    public Result<?> sendMessage1(@RequestBody String string) {
        //数据判空
        if (string.isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.DATA_EMPTY);
        }

        MessageEntity messageEntity = JSONObject.parseObject(string, MessageEntity.class);
        producerService.sendTopic1(messageEntity);
        return Result.ok("发送消息成功");
    }

    //发送消息
    @PostMapping("/send2")
    public Result<?> sendMessage2(@RequestBody String string) {
        //数据判空
        if (string.isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.DATA_EMPTY);
        }

        MessageEntity messageEntity = JSONObject.parseObject(string, MessageEntity.class);
        producerService.sendTopic2(messageEntity);
        return Result.ok("发送消息成功");
    }

    //test
    @PostMapping("/test")
    public Result<?> test(Integer integer) {
        //数据判空
        if (integer.equals(0)) {
            throw new SystemException(AppHttpCodeEnum.DATA_EMPTY);
        }

        return Result.ok("ok");
    }

}
