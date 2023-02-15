package com.bank.entity;


import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class OrderInfo  {

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

    public OrderInfo(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}
