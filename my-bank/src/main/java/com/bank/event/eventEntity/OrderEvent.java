package com.bank.event.eventEntity;

import com.bank.entity.OrderInfo;

public class OrderEvent extends BaseEvent<OrderInfo> {

    private static final long serialVersionUID = 6374994069212223260L;

    public OrderEvent(Object source, Integer type, OrderInfo obj) {
        super(source, type, obj);
    }

}
