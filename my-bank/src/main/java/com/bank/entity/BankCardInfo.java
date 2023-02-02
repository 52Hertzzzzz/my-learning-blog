package com.bank.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("serial")
@TableName("bank_card_info")
public class BankCardInfo {

    @NotBlank(message = "银行名称不能为空")
    String bankName;

    String bankCardNum;

    //@NotBlank不能用于数字，Integer用@NotNull
    //@Size()只能用于数组或列表，判断字符长度用@Length
    @NotBlank(message = "密码不能为空")
    @Length(min = 4, max= 8, message = "请将密码设置为4-8位")
    String password;

    Double balance;

    @TableField(fill = FieldFill.INSERT)
    Long createBy;

    @TableField(fill = FieldFill.INSERT)
    Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    Long updateBy;

    @TableField(fill = FieldFill.UPDATE)
    Date updateTime;

}
