package com.bank.service.impl;

import com.bank.entity.OrderInfo;
import com.bank.entity.StuffInfo;
import com.bank.listener.OrderExpireListener;
import com.bank.mapper.BankCardMapper;
import com.bank.mapper.OrderInfoMapper;
import com.bank.mapper.StuffInfoMapper;
import com.bank.service.PayService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.framework.utils.Result;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class PayServiceImpl implements PayService {

    @Autowired
    private StuffInfoMapper stuffInfoMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private OrderExpireListener orderExpireListener;

    @Override
    public IPage<StuffInfo> listStuffs(Page<StuffInfo> page, String stuffName) {
        LambdaQueryWrapper<StuffInfo> wrapper = new LambdaQueryWrapper<>();
        //eq 等于 ne不等于
        //lt 小于 le 小于等于
        //gt 大于 ge 大于等于
        wrapper.eq(StuffInfo::getStatus, 0);
        wrapper.gt(StuffInfo::getAmount, 0);
        if (!Strings.isNullOrEmpty(stuffName)) {
            wrapper.like(StuffInfo::getName, stuffName);
        }

        IPage<StuffInfo> result = stuffInfoMapper.selectPage(page, wrapper);
        return result;
    }

    @Override
    //@Transactional(rollbackFor = Exception.class)
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
        //LambdaQueryWrapper<StuffInfo> wrapper = new LambdaQueryWrapper<>();
        //wrapper.eq(StuffInfo::getStuffId, orderInfo.getStuffId())
        //        .eq(StuffInfo::getStatus, 0);
        //StuffInfo stuffInfo = stuffInfoMapper.selectOne(wrapper);

        //查询库存需要使用排他锁，否则会并发超卖
        String stuffId = orderInfo.getStuffId();
        Long stuffAmount = stuffInfoMapper.getStuffAmount(stuffId);
        if (Objects.isNull(stuffAmount)) {
            return Result.error("物品不存在");
        }

        if (stuffAmount < orderInfo.getStuffCount()) {
            return Result.error("库存不足，请重新下单");
        }

        //下订单
        String orderId = IdWorker.get32UUID();
        orderInfo.setOrderId(orderId);
        orderInfo.setPaymentStatus(0);
        int insert = orderInfoMapper.insert(orderInfo);
        orderExpireListener.offerTask(orderId);

        //减库存
        stuffAmount -= orderInfo.getStuffCount();
        int update = stuffInfoMapper.updateStuffAmount(stuffId, stuffAmount);

        return Result.ok("下单成功，请尽快支付");
    }

}
