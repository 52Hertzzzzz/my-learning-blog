package com.consumer.service.impl;

import com.framework.entity.EMail;
import com.framework.utils.MailUtils;
import com.framework.utils.RedisUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@RabbitListener(queues = "order.email")
public class EMailConsumer {

    Logger logger = LoggerFactory.getLogger(EMailConsumer.class);

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    private static final String RETRY_EXECUTE_TIMES_KEY = "retryExecuteTimes";

    private static final Integer RETRY_EXECUTE_TIMES_MAX = 3;

    @Autowired
    private RedisUtil RedisUtil;

    @RabbitHandler
    public void listenTopicQueue1(Channel channel, Message message, EMail eMail, @Headers Map<String, Object> headers) {
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
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("已经报错了哈");

            //手动实现重试
            try {
                retryExecute(channel, deliveryTag, eMail.getMessageId());
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            //手动Nack与retry自动放入死信队列互斥，最好只使用一个，使用其他方法实现重试
            //throw new RuntimeException();
        }
    }

    //利用Redis手动实现重试机制
    private void retryExecute(Channel channel, long deliveryTag, String messageId) throws IOException {
        String redisKey = RETRY_EXECUTE_TIMES_KEY.concat(":").concat(messageId);
        Object value = RedisUtil.get(redisKey);
        if (Objects.isNull(value)){
            //当前为第一次执行，返回重试
            RedisUtil.set(redisKey, 2, 60 * 5);
            channel.basicNack(deliveryTag, false, true);
        } else {
            Integer integer = Integer.parseInt(value.toString());
            if (integer < RETRY_EXECUTE_TIMES_MAX) {
                //当前为第二次执行，返回重试
                RedisUtil.set(redisKey, integer + 1, 60 * 5);
                channel.basicNack(deliveryTag, false, true);
            } else {
                log.error("3次了，不试了，扔死信队列了");
                channel.basicNack(deliveryTag, false, false);
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
