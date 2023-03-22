package com.bank.mapper;

import com.bank.entity.OrderInfo;
import com.bank.vo.OrderInfoResponseVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.mapping.ResultSetType;

import java.util.List;


/**
 * (OrderInfo)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-14 16:34:24
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = Integer.MIN_VALUE)
    @ResultType(OrderInfoResponseVo.class)
    Cursor<OrderInfoResponseVo> listOrders(@Param("userName") String userName);

}
