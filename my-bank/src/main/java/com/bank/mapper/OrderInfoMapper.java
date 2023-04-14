package com.bank.mapper;

import com.bank.entity.OrderInfo;
import com.bank.vo.OrderInfoResponseVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.ResultHandler;


/**
 * (OrderInfo)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-14 16:34:24
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = Integer.MIN_VALUE)
    @ResultType(OrderInfoResponseVo.class)
    @Select("SELECT *, 0 orderType FROM `order_info`\n" +
            "        WHERE username = #{userName}")
    Cursor<OrderInfoResponseVo> listOrders(@Param("userName") String userName);

    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = Integer.MIN_VALUE)
    @ResultType(OrderInfoResponseVo.class)
    @Select("SELECT *, 0 orderType FROM `order_info`\n" +
            "        WHERE username = #{userName}")
    void listOrders2(@Param("userName") String userName, ResultHandler<OrderInfoResponseVo> handler);

}
