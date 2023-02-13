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
@RabbitListener(queues = "order.email")
public class EMailConsumer {

    Logger logger = LoggerFactory.getLogger(PayConsumer.class);

    @RabbitHandler
    public void listenTopicQueue1(MessageEntity entity) {
        logger.info("Queue:order.email master get: {}", entity.toString());
    }

    //Signal:不允许出现一个Listener下两个消费方法，否则会报错
    //Ambiguous methods for payload type 模棱两可的消费方法!!!

    //@RabbitHandler
    //public void listenTopicQueue2(MessageEntity entity) {
    //    logger.info("Queue:order.email slave get: {}", entity.toString());
    //}

}
