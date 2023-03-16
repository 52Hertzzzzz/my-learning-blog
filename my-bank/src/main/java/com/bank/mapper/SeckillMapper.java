package com.bank.mapper;

import com.bank.vo.OrderInfoResponseVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;

public interface SeckillMapper {

    Cursor<OrderInfoResponseVo> listOrders(@Param("userName") String userName);

}
