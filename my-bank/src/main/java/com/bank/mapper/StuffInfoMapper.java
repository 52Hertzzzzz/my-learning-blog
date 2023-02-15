package com.bank.mapper;

import com.bank.entity.OrderInfo;
import com.bank.entity.StuffInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * (StuffInfo)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-14 16:35:47
 */
public interface StuffInfoMapper extends BaseMapper<StuffInfo> {

    int addStuffs(List<StuffInfo> stuffsList);

    Long getStuffAmount(String stuffId);

    int updateStuffAmount(@Param("stuffId") String stuffId, @Param("stuffAmount") Long stuffAmount);

}
