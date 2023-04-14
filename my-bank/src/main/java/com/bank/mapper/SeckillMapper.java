package com.bank.mapper;

import com.bank.vo.OrderInfoResponseVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.ResultHandler;

public interface SeckillMapper {

//    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = Integer.MIN_VALUE)
//    @ResultType(OrderInfoResponseVo.class)
    Cursor<OrderInfoResponseVo> listOrders(@Param("userName") String userName);

    void listOrders2(@Param("userName") String userName, ResultHandler<OrderInfoResponseVo> handler);

}
