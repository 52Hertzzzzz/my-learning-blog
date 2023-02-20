package com.bank.listener;

import com.bank.entity.OrderInfo;
import com.bank.mapper.OrderInfoMapper;
import com.bank.mapper.StuffInfoMapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.framework.entity.EMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

@Component
public class OrderExpireListener {

    Logger log = LoggerFactory.getLogger(OrderExpireListener.class);

    private static final int THREAD_NUM = 3;

    private static final int DELAY_SECONDS = 3;

    private Thread[] threads = new Thread[THREAD_NUM];

    //private Thread[] threads;

    private DelayQueue<OrderExpireTask> orderExpireTasks = new DelayQueue<>();

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private StuffInfoMapper stuffInfoMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 初始化启动线程
     */
    @PostConstruct
    public void init() {
        for (int i = 0; i < THREAD_NUM; i++) {
            threads[i] = new Thread(() -> {
                execute();
            });
            threads[i].setDaemon(true);
            threads[i].setName("OrderExpireListener-" + (i + 1));
            threads[i].start();
        }
    }

    public void offerTask(OrderInfo orderInfo) {
        long timeout = TimeUnit.NANOSECONDS.convert(DELAY_SECONDS, TimeUnit.SECONDS);
        this.orderExpireTasks.offer(new OrderExpireTask(orderInfo, timeout));
        System.out.println("OrderExpireTasks Size Now: " + orderExpireTasks.size());
    }

    private void execute() {
        for (;;) {
            try {
                OrderExpireTask task = orderExpireTasks.take();
                if (Objects.nonNull(task)) {
                    OrderInfo orderInfo = task.getOrderInfo();
                    LambdaUpdateWrapper<OrderInfo> wrapper = new LambdaUpdateWrapper<>();
                    wrapper.eq(OrderInfo::getOrderId, orderInfo.getOrderId());
                    wrapper.eq(OrderInfo::getPaymentStatus, 0);
                    //只更新指定字段，直接用wrapper的set方法
                    //更新多个字段，new一个实体类传入Update方法
                    wrapper.set(OrderInfo::getPaymentStatus, 2);
                    int update = orderInfoMapper.update(null, wrapper);
                    if (update > 0) {
                        log.info("超时订单 {} 已取消，通知用户", orderInfo.getOrderId());

                        //Todo:归还库存逻辑
                        int returnStuff = stuffInfoMapper.returnStuff(orderInfo.getStuffId(), orderInfo.getStuffCount());

                        EMail eMail = new EMail();
                        eMail.setAddress("425633796@qq.com");
                        eMail.setSubject("Your order has been canceled.");
                        eMail.setContent("<h1>Your order has been canceled.</h1><br/><h2>:(</h2>");
                        rabbitTemplate.convertAndSend("my.order", "order.email", eMail);
                    } else {
                        log.info("订单 {} 已支付");
                    }

                    //LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper();
                    //wrapper.eq(OrderInfo::getOrderNum, task.getOrderNum());
                    //wrapper.eq(OrderInfo::getPaymentStatus, 0);
                    //OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);
                    //if (Objects.nonNull(orderInfo)) {
                    //    UpdateWrapper<OrderInfo> wrapper1 = new UpdateWrapper<>();
                    //    wrapper.eq(OrderInfo::getOrderNum, task.getOrderNum());
                    //    wrapper.eq(OrderInfo::getPaymentStatus, 0);
                    //    int update = orderInfoMapper.update();
                    //}
                    //orderInfoMapper.selectOne();
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("过期订单延时队列获取异常");
            }
        }
    }

}