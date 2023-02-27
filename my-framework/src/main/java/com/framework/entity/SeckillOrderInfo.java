package com.framework.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.extern.slf4j.Slf4j;

/**
 * (SeckillOrderInfo)表实体类
 *
 * @author makejava
 * @since 2023-02-27 11:05:22
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("seckill_order_info")
@Slf4j
public class SeckillOrderInfo extends AbstractMessage {

    @TableId
    private Long id;

    private String orderId;

    private String username;

    private String stuffId;

    private Double moneyAmount;

    private Integer paymentStatus;

    @TableField(fill = FieldFill.INSERT)
    Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    Date updateTime;

}
