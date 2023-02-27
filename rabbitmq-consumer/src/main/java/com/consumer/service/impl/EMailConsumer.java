package com.consumer.service.impl;

import com.framework.entity.EMail;
import com.framework.utils.MailUtils;
import com.framework.utils.RedisUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@RabbitListener(queues = "order.email")
public class EMailConsumer extends AbstractConsumer {

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    @Autowired
    public EMailConsumer(RedisUtil redisUtil) {
        super(redisUtil);
    }

    @RabbitHandler
    @SuppressWarnings("all")
    public void listenTopicQueue1(Channel channel, Message message, EMail eMail, @Headers Map<String, Object> headers) {
        log.info("Queue:order.email master get: {}", eMail.toString());
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        //atomicInteger.getAndIncrement();
        try {
            //手动触发报错
            if (atomicInteger.get() == 0) {
                log.error("该报错了哈");
                throw new RuntimeException();
            }

            MailUtils.sendMail(eMail.getAddress(), eMail.getSubject(), eMail.getContent(), true);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("已经报错了哈");

            //手动实现重试
            try {
                this.retryExecute(channel, message, eMail, headers);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            //手动Nack与retry自动放入死信队列互斥，最好只使用一个，使用其他方法实现重试
            //throw new RuntimeException();
        }
    }

    //Signal:不允许出现一个Listener下两个消费方法，否则会报错
    //Ambiguous methods for payload type 模棱两可的消费方法!!!

    //@RabbitHandler
    //public void listenTopicQueue2(MessageEntity entity) {
    //    logger.info("Queue:order.email slave get: {}", entity.toString());
    //}

}
