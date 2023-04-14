package com.bank.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * (OrderInfo)表实体类
 *
 * @author makejava
 * @since 2023-02-14 16:34:24
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("order_info")
@Slf4j
public class OrderInfo {

    @TableId
    private Long id;
    
    private String orderId;
    
    private String username;
    
    private String stuffId;
    
    private Integer stuffCount;
    
    private Double moneyAmount;

    private Integer paymentStatus;

    @TableField(fill = FieldFill.INSERT)
    Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    Date updateTime;

}
