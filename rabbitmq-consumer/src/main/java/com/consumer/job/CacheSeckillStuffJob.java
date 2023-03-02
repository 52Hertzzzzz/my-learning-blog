package com.consumer.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.consumer.mapper.SeckillStuffInfoMapper;
import com.framework.entity.SeckillStuffInfo;
import com.framework.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class CacheSeckillStuffJob {

    @Autowired
    private SeckillStuffInfoMapper mapper;

    @Autowired
    private RedisUtil redisUtil;

    private static final String redisKey = "SeckillStuffCount";

    /***
     * 每10分钟将秒杀产品数量放入redis
     */
    @Scheduled(cron = "0 0/3 * * * ?")
    public void invoke() {
        LambdaQueryWrapper<SeckillStuffInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SeckillStuffInfo::getStatus, 0);
        wrapper.gt(SeckillStuffInfo::getStuffCount, 0);
        List<SeckillStuffInfo> list = mapper.selectList(wrapper);

        if (Objects.nonNull(list)) {
            list.stream()
                .forEach(entity -> redisUtil.hset(redisKey, entity.getStuffId(), entity.getStuffCount().intValue()));
        }
    }

}
