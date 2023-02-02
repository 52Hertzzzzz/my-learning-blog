package com.bank.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("serial")
@TableName("credit_card_info")
public class CreditCardInfo {

    String bankCardNum;

    Double moneyLimit;

    @TableField(fill = FieldFill.INSERT)
    Long createBy;

    @TableField(fill = FieldFill.INSERT)
    Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    Long updateBy;

    @TableField(fill = FieldFill.UPDATE)
    Date updateTime;

}
