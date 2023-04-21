package com.bank.event.eventListener;

import com.bank.entity.BankUserInfo;
import com.bank.entity.OrderInfo;
import com.bank.event.eventEntity.OrderEvent;
import com.bank.mapper.BankUserMapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderEventListener {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final BigDecimal multiply = new BigDecimal(0.1);

    @Autowired
    private BankUserMapper bankUserMapper;

    @EventListener(value = {OrderEvent.class}, condition = "#event.type == 0")
    @Async("common")
    public void invoke(OrderEvent event) {
        try {
            String name = Thread.currentThread().getName();
            log.info("当前线程: {}", name);
            log.info("模拟3秒处理时长");
            Thread.sleep(1000 * 3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        OrderInfo obj = event.getObj();
        String orderId = obj.getOrderId();
        String username = obj.getUsername();
        BigDecimal moneyAmount = new BigDecimal(obj.getMoneyAmount());
        log.info("用户: {}, 订单: {}, 积分事件开始处理", username, orderId);

        double returnPoints = moneyAmount.multiply(multiply).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        LambdaUpdateWrapper<BankUserInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(BankUserInfo::getUserName, username);
        wrapper.setSql("user_points = user_points + " + returnPoints);
        bankUserMapper.update(null, wrapper);
    }

}
