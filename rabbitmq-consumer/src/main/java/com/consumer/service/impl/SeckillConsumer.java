package com.consumer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.consumer.mapper.SeckillOrderInfoMapper;
import com.consumer.mapper.SeckillStuffInfoMapper;
import com.framework.entity.SeckillOrderInfo;
import com.framework.entity.SeckillStuffInfo;
import com.framework.utils.RedisUtil;
import com.google.common.collect.Lists;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RabbitListener(queues = "order.seckill")
public class SeckillConsumer extends AbstractConsumer {

    @Autowired
    private SeckillOrderInfoMapper seckillOrderInfoMapper;

    @Autowired
    private SeckillStuffInfoMapper seckillStuffInfoMapper;

    private static final String lockKey = "RabbitMQLock:SeckillConsumer";

    @Autowired
    public SeckillConsumer(RedisUtil redisUtil) {
        super(redisUtil);
    }

    @RabbitHandler
    @Transactional(rollbackFor = Exception.class)
    public void listenTopicQueue1(Channel channel, Message message, SeckillOrderInfo seckillOrderInfo, @Headers Map<String, Object> headers) {
        log.info("Queue:order.seckill master get: {}", seckillOrderInfo.toString());
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String lockId = String.valueOf(Thread.currentThread().getId());

        try {
            //New:新增分布式锁
            Boolean lock = lock(lockKey, lockId, 3, TimeUnit.SECONDS);

            //下订单
            String orderId = IdWorker.get32UUID();
            seckillOrderInfo.setOrderId(orderId);

            LambdaQueryWrapper<SeckillStuffInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SeckillStuffInfo::getStuffId, seckillOrderInfo.getStuffId());
            wrapper.eq(SeckillStuffInfo::getStatus, 0);
            SeckillStuffInfo seckillStuffInfo = seckillStuffInfoMapper.selectOne(wrapper);
            Long stuffCount = seckillStuffInfo.getStuffCount();
            if (stuffCount > 0) {
                log.info("库存充足");
                seckillOrderInfo.setPaymentStatus(0);
                int insert = seckillOrderInfoMapper.insert(seckillOrderInfo);
                int update = seckillStuffInfoMapper.decreaseStuffCount(seckillOrderInfo.getStuffId());
            } else {
                log.info("库存不足");
                seckillOrderInfo.setPaymentStatus(2);
                int insert = seckillOrderInfoMapper.insert(seckillOrderInfo);
            }

            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            e.printStackTrace();
            //手动实现重试
            try {
                this.retryExecute(channel, message, seckillOrderInfo, headers);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } finally {
            if (lockId.equals((String) redisUtil.get(lockKey))) {
                //释放锁
                redisUtil.del(lockKey);
            }
        }
    }

}
