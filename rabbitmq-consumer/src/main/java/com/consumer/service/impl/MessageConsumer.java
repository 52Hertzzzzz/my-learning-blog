package com.consumer.service.impl;

import com.framework.entity.MessageEntity;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageConsumer {

    Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    @RabbitListener(queues = "order.pay")
    public void listenTopicQueue1(MessageEntity entity) {
        logger.info("Queue:order.pay get: {}", entity.toString());
    }

    @RabbitListener(queues = "order.pay")
    public void listenTopicQueue2(MessageEntity entity) {
        logger.info("Queue:order.pay slave get: {}", entity.toString());
    }

}
