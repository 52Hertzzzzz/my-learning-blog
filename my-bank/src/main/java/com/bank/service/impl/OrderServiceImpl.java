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

import java.io.IOException;
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

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public List<OrderInfoResponseVo> listOrders(String userName) throws Exception {
        //大数据量非分页查询，使用流式查询/游标查询
        //查询组装两种订单
        ArrayList<OrderInfoResponseVo> res = Lists.newArrayList();
        Cursor<OrderInfoResponseVo> cursor1 = null;
        try {
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
        try (Cursor<OrderInfoResponseVo> cursor2 = seckillMapper.listOrders(userName)) {
            cursor2.forEach(e -> res.add(e));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }

        return res;
    }

}
