package com.consumer.service.impl;

import com.framework.entity.EMail;
import com.framework.utils.MailUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@RabbitListener(queues = "order.email")
public class EMailConsumer {

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    Logger logger = LoggerFactory.getLogger(EMailConsumer.class);

    @RabbitHandler
    public void listenTopicQueue1(Channel channel, Message message, EMail eMail) {
        logger.info("Queue:order.email master get: {}", eMail.toString());
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        //atomicInteger.getAndIncrement();
        try {

            //手动触发报错
            if (atomicInteger.get() == 0) {
                log.error("该报错了哈");
                throw new RuntimeException();
            }

            MailUtils.sendMail(eMail.getAddress(), eMail.getSubject(), eMail.getContent(), true);
            //channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("已经报错了哈");

            //try {
            //    //需要Nack才能进入死信队列
            //    log.info("我Nack一下吧");
            //    channel.basicNack(deliveryTag, false, false);
            //    log.info("我Nack完了");
            //} catch (Exception ex) {
            //    log.info("我Nack失败了");
            //    log.error(ex.getMessage());
            //}

            throw new RuntimeException();
        }
    }

    //Signal:不允许出现一个Listener下两个消费方法，否则会报错
    //Ambiguous methods for payload type 模棱两可的消费方法!!!

    //@RabbitHandler
    //public void listenTopicQueue2(MessageEntity entity) {
    //    logger.info("Queue:order.email slave get: {}", entity.toString());
    //}

}
