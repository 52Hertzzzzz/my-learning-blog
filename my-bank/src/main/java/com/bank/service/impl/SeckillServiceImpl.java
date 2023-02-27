package com.bank.service.impl;

import com.bank.service.SeckillService;
import com.framework.entity.SeckillOrderInfo;
import com.framework.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisUtil redisUtil;

    private String SECKILL_STUFF_COUNT_KEY = "SeckillStuffCount";

    @Override
    public int seckillStuff(SeckillOrderInfo orderInfo) {
        int res = 0;
        try {
            Integer count = (Integer) redisUtil.hget(SECKILL_STUFF_COUNT_KEY, orderInfo.getStuffId());
            if (count > 0) {
                log.info("物品库存充足，可以购买");
                redisUtil.hdecr(SECKILL_STUFF_COUNT_KEY, orderInfo.getStuffId(), 1);
                rabbitTemplate.convertAndSend("my.order", "order.seckill", orderInfo);
                res = 1;
            } else {
                log.error("物品库存不足");
            }
        } catch (Exception e) {
            log.error("物品不存在");
        }

        return res;
    }

}
