package com.bank.event.eventEntity;

import org.springframework.context.ApplicationEvent;

public class BaseEvent<T> extends ApplicationEvent {

    private static final long serialVersionUID = 6370223317981500282L;

    //事件类型，可扩展
    private Integer type;

    protected T obj;

    public BaseEvent(Object source, Integer type, T obj) {
        super(source);
        this.type = type;
        this.obj = obj;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

}
