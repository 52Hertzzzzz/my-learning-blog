package com.consumer.service.impl;

import com.framework.entity.AbstractMessage;
import com.framework.utils.RedisUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Slf4j
public abstract class AbstractConsumer {

    private static final String RETRY_EXECUTE_TIMES_KEY = "retryExecuteTimes";

    private static final Integer RETRY_EXECUTE_TIMES_MAX = 3;

    private RedisUtil redisUtil;

    public AbstractConsumer(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    //利用Redis手动实现重试机制
    public void retryExecute(Channel channel, Message message, AbstractMessage abstractMessage, Map<String, Object> headers) throws IOException {
        MessageProperties messageProperties = message.getMessageProperties();
        String redisKey = RETRY_EXECUTE_TIMES_KEY.concat(":").concat(abstractMessage.getMessageId());
        Object value = redisUtil.get(redisKey);
        if (Objects.isNull(value)) {
            //当前为第一次执行，返回重试
            redisUtil.set(redisKey, 2, 60 * 5);
            channel.basicNack(messageProperties.getDeliveryTag(), false, true);
        } else {
            Integer integer = Integer.parseInt(value.toString());
            if (integer < RETRY_EXECUTE_TIMES_MAX) {
                //当前为第二次执行，返回重试
                redisUtil.set(redisKey, integer + 1, 60 * 5);
                channel.basicNack(messageProperties.getDeliveryTag(), false, true);
            } else {
                log.error("3次了，不试了，扔死信队列了");
                channel.basicNack(messageProperties.getDeliveryTag(), false, false);
            }
        }
    }

}
