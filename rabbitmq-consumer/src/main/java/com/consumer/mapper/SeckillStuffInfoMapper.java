package com.consumer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.framework.entity.SeckillStuffInfo;
import org.apache.ibatis.annotations.Param;


/**
 * (SeckillStuffInfo)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-27 10:46:04
 */
public interface SeckillStuffInfoMapper extends BaseMapper<SeckillStuffInfo> {

    int decreaseStuffCount(@Param("stuffId") String stuffId);

}
