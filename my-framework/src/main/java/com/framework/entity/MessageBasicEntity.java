package com.framework.entity;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.Data;

import java.io.Serializable;

@Data
public class MessageBasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String MessageId;

    MessageBasicEntity() {
        this.MessageId = IdWorker.get32UUID();
    }

}
