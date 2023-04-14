package com.bank.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("serial")
public class BankRegisterVo {

    @NotBlank(message = "银行名称不能为空")
    String bankName;

    String bankCardNum;

    //@NotBlank不能用于数字，Integer用@NotNull
    //@Size()只能用于数组或列表，判断字符长度用@Length
    //@NotBlank(message = "密码不能为空")
    //@Length(min = 4, max= 8, message = "请将密码设置为4-8位")
    String password;

    Double balance;

    String userName;

    String sexual;

    Integer age;

    Double moneyLimit;

    Long createBy;

    Date createTime;

    Long updateBy;

    Date updateTime;

}
