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

    Logger logger = LoggerFactory.getLogger(PayConsumer.class);

    @RabbitHandler
    public void listenTopicQueue1(Channel channel, Message message, EMail eMail) {
        logger.info("Queue:order.email master get: {}", eMail.toString());
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        atomicInteger.getAndIncrement();
        try {
            //if (atomicInteger.get() % 2 == 0) {
            //    throw new RuntimeException();
            //}
            MailUtils.sendMail(eMail.getAddress(), eMail.getSubject(), eMail.getContent(), true);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                channel.basicNack(deliveryTag, false, false);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //Signal:不允许出现一个Listener下两个消费方法，否则会报错
    //Ambiguous methods for payload type 模棱两可的消费方法!!!

    //@RabbitHandler
    //public void listenTopicQueue2(MessageEntity entity) {
    //    logger.info("Queue:order.email slave get: {}", entity.toString());
    //}

}
