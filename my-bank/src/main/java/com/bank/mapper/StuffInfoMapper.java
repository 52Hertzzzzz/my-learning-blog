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

    Long getStuffCount(String stuffId);

    int updateStuffCount(@Param("stuffId") String stuffId, @Param("stuffCount") Long stuffCount);

    int returnStuff(@Param("stuffId") String stuffId, @Param("stuffCount") Integer stuffCount);

}
