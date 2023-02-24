package com.consumer.service.impl;

import com.framework.entity.MessageEntity;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RabbitListener(queues = "order.seckill")
public class SeckillConsumer {


    @RabbitHandler
    public void listenTopicQueue1(MessageEntity entity) {
        log.info("Queue:order.seckill master get: {}", entity.toString());
    }

}
