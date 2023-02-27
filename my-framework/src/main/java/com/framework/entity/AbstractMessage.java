package com.framework.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.Data;

import java.io.Serializable;

@Data
public abstract class AbstractMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String MessageId;

    AbstractMessage() {
        this.MessageId = IdWorker.get32UUID();
    }

}
