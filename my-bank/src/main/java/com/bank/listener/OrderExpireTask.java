package com.bank.listener;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/***
 * 延时队列存放对象，DelayQueue中必须存放Delayed实现类
 *
 */
public class OrderExpireTask implements Delayed {

    /**
     * 延迟时长
     */
    private long excuteTime;

    /**
     * 订单号
     */
    private String orderNum;

    public OrderExpireTask(String orderNum, long delayTime) {
        this.excuteTime = System.nanoTime() + delayTime;
        this.orderNum = orderNum;
    }

    /***
     * 队列内任务排序方法，相当于编写Comparator逻辑，整个队列为执行时间升序排序
     * diff = 当前任务执行时间戳 - 其他任务执行时间戳
     * diff > 0 说明当前任务后执行，将其后置
     * diff <= 0 说明当前任务先执行，不交换位置
     * @param o
     * @return
     */
    @Override
    public int compareTo(Delayed o) {
        OrderExpireTask other = (OrderExpireTask) o;
        long diff = excuteTime - other.excuteTime;
        return diff > 0 ? 1 : (diff < 0 ? -1 : 0);
    }

    /***
     * 获取当前任务是否到达执行时间
     * time = 当前任务执行时间戳 - 当前时间戳
     * time > 0 还未到达执行时间
     * time <= 0 已到达执行时间
     * @param unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.excuteTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

}
