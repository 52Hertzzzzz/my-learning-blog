package com.bank.listener;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/***
 * 延时队列存放对象，DelayQueue中必须存放Delayed实现类
 *
 */
public class DelayQueueTask implements Delayed {


    @Override
    public long getDelay(TimeUnit unit) {
        return 0;
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }
}
