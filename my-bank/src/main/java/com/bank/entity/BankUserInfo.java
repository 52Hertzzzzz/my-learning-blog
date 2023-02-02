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
@TableName("bank_user_info")
public class BankUserInfo {

    String userName;

    String bankCardNum;

    String password;

    String sexual;

    Integer age;

    @TableField(fill = FieldFill.INSERT)
    Long createBy;

    @TableField(fill = FieldFill.INSERT)
    Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    Long updateBy;

    @TableField(fill = FieldFill.UPDATE)
    Date updateTime;

}