package com.bank.entity;


import java.io.Serializable;
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
    
    private String orderNum;
    
    private String username;
    
    private String stuffId;
    
    private Integer stuffCount;
    
    private Double moneyAmount;
    
    private Integer paymentStatus;

}
