package com.bank.service.impl;

import com.bank.entity.OrderInfo;
import com.bank.entity.StuffInfo;
import com.bank.listener.OrderExpireListener;
import com.bank.mapper.BankCardMapper;
import com.bank.mapper.OrderInfoMapper;
import com.bank.mapper.SeckillMapper;
import com.bank.mapper.StuffInfoMapper;
import com.bank.service.OrderService;
import com.bank.vo.OrderInfoResponseVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.framework.utils.Result;
import com.google.common.collect.Lists;
import org.apache.ibatis.cursor.Cursor;
import org.assertj.core.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {

    Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private StuffInfoMapper stuffInfoMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private SeckillMapper seckillMapper;

    @Autowired
    private OrderExpireListener orderExpireListener;

    @Override
    public IPage<StuffInfo> listStuffs(Page<StuffInfo> page, String stuffName) {
        LambdaQueryWrapper<StuffInfo> wrapper = new LambdaQueryWrapper<>();
        //eq 等于 ne不等于
        //lt 小于 le 小于等于
        //gt 大于 ge 大于等于
        wrapper.eq(StuffInfo::getStatus, 0);
        wrapper.gt(StuffInfo::getStuffCount, 0);
        if (!Strings.isNullOrEmpty(stuffName)) {
            wrapper.like(StuffInfo::getName, stuffName);
        }

        IPage<StuffInfo> result = stuffInfoMapper.selectPage(page, wrapper);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addStuffs(List<StuffInfo> stuffsList) {
        //stuffInfoMapper.insert(new StuffInfo(10L, IdWorker.get32UUID(), "我试试事务", 1.11, 10L, 0));
        stuffsList.stream().forEach(entity -> entity.setStuffId(IdWorker.get32UUID()));
        int integer = stuffInfoMapper.addStuffs(stuffsList);

        //int i = 1/0;
        return integer;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> addOrder(OrderInfo orderInfo) {

        //查询库存需要使用排他锁，否则会并发超卖
        String stuffId = orderInfo.getStuffId();
        Long stuffAmount = stuffInfoMapper.getStuffCount(stuffId);
        if (Objects.isNull(stuffAmount)) {
            log.error("物品不存在");
            return Result.error("物品不存在");
        }

        if (stuffAmount < orderInfo.getStuffCount()) {
            log.error("库存不足，请重新下单");
            return Result.error("库存不足，请重新下单");
        }

        //下订单
        String orderId = IdWorker.get32UUID();
        orderInfo.setOrderId(orderId);
        if (Objects.isNull(orderInfo.getPaymentStatus())) {
            orderInfo.setPaymentStatus(0);
        }
        int insert = orderInfoMapper.insert(orderInfo);
        orderExpireListener.offerTask(orderInfo);

        //减库存
        stuffAmount -= orderInfo.getStuffCount();
        int update = stuffInfoMapper.updateStuffCount(stuffId, stuffAmount);

        return Result.ok("下单成功，请尽快支付");
    }

    /***
     * 以下两种流式查询rowData都是 {ResultsetRowsStreaming.class}
     * 1.RowDataStatic 静态结果集，默认的查询方式，普通查询
     * 2.RowDataDynamic 动态结果集，流式查询
     * 3.RowDataCursor 游标结果集，服务器端基于游标查询
     * @param userName
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public List<OrderInfoResponseVo> listOrders(String userName) throws Exception {
        //大数据量非分页查询，使用流式查询/游标查询
        //查询组装两种订单
        ArrayList<OrderInfoResponseVo> res = Lists.newArrayList();
        Cursor<OrderInfoResponseVo> cursor1 = null;
        try {

            //流式查询1，所有条件都以注解形式放在Mapper接口上方
            //不可以Mapper接口加注解，xml文件写SQL语句！！！
            //@Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = Integer.MIN_VALUE)
            //@ResultType(OrderInfoResponseVo.class)
            //@Select(sql)
            cursor1 = orderInfoMapper.listOrders(userName);
            cursor1.forEach(e -> res.add(e));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            //资源关闭方式1: finally 手动关闭
            if (Objects.nonNull(cursor1)) {
                cursor1.close();
            }
        }

        //资源关闭方式2: try with resource句式

        //流式查询2，所有条件都加在xml标签中
        //包括ResultType、resultSetType、fetchSize
        //<select id="listOrders" resultType="com.bank.vo.OrderInfoResponseVo" resultSetType="FORWARD_ONLY" fetchSize="-2147483648">
        try (Cursor<OrderInfoResponseVo> cursor2 = seckillMapper.listOrders(userName)) {
            cursor2.forEach(e -> res.add(e));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }

        return res;
    }

    /***
     * 以下两种流式查询rowData也都是 {ResultsetRowsStreaming.class}
     * 和Curosr为返回值的方式基本一致，唯一不同为无返回值，使用自定义的ResultHandler对数据进行处理
     * @param userName
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public List<OrderInfoResponseVo> listOrders2(String userName) throws Exception {
        //大数据量非分页查询，使用流式查询/游标查询
        //查询组装两种订单
        ArrayList<OrderInfoResponseVo> res = Lists.newArrayList();
        try {

            //流式查询3，所有条件都以注解形式放在Mapper接口上方
            orderInfoMapper.listOrders2(userName, obj -> res.add(obj.getResultObject()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }

        //流式查询4，所有条件都加在xml标签中
        try {
            seckillMapper.listOrders2(userName, obj -> res.add(obj.getResultObject()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }

        return res;
    }

}
