package com.producer.serivce.impl;

import com.framework.entity.MessageEntity;
import com.producer.serivce.ProducerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProducerServiceImpl implements ProducerService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //交换机名
    private static final String exchangeName = "my.order";

    //队列名
    private static final String queueName1 = "order.pay";
    private static final String queueName2 = "order.pay2";

    @Override
    public void sendTopic1(MessageEntity entity) {
        rabbitTemplate.convertAndSend(exchangeName, queueName1, entity);
    }

    @Override
    public void sendTopic2(MessageEntity entity) {
        rabbitTemplate.convertAndSend(exchangeName, queueName2, entity);
    }

}
