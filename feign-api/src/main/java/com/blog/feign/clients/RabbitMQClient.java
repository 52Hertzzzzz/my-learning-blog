package com.blog.feign.clients;

import com.framework.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "mqproducer-service")
public interface RabbitMQClient {

    //需要把token放入请求头中，否则消息发送模块无法通过验证
    //需要指定Post请求，否则会默认发出Get请求导致远程接口调用异常
    @PostMapping("/message/send1")
    Result<?> send1(@RequestBody String string);

}
