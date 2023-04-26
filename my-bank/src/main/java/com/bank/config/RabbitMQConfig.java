package com.bank.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class RabbitMQConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(this::confirm);
        rabbitTemplate.setReturnCallback(this::returnedMessage);
    }

    //@Bean
    //public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    //    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    //    rabbitTemplate.setMandatory(true);
    //    rabbitTemplate.setConfirmCallback(this::confirm);
    //    rabbitTemplate.setReturnCallback(this::returnedMessage);
    //    return rabbitTemplate;
    //}

    //生产者 -> 交换机消息回调
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        if (b) {
            log.info("发送至交换机成功");
        } else {
            log.error("发送至交换机失败, CorrelationData: {}, Reason: {}", correlationData, s);
        }
    }

    //交换机 -> 队列消息失败回调
    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        log.error("发送至队列失败, Message: {}", message);
    }

}
