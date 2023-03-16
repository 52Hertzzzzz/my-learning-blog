package com.bank.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfoResponseVo {

    @TableId
    private Long id;

    private String orderId;

    private String username;

    private String stuffId;

    //秒杀订单默认为1
    private Integer stuffCount;

    private Double moneyAmount;

    private Integer paymentStatus;

    //0 普通订单, 1 秒杀订单
    private Integer orderType;

    private Date createTime;

    private Date updateTime;

}
