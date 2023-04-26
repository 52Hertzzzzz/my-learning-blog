package com.consumer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.framework.entity.SeckillOrderInfo;


/**
 * (SeckillOrderInfo)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-27 11:05:21
 */
public interface SeckillOrderInfoMapper extends BaseMapper<SeckillOrderInfo> {

    int insertSeckillOrderInfo(SeckillOrderInfo seckillOrderInfo);

}
