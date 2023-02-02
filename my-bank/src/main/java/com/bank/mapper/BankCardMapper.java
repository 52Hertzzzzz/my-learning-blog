package com.bank.mapper;

import com.bank.entity.BankCardInfo;
import com.bank.entity.BankUserInfo;
import com.bank.entity.CreditCardInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface BankCardMapper extends BaseMapper<BankCardInfo> {

    Integer registerUser(BankUserInfo bankUserInfo);

    Integer applyCreditCard(CreditCardInfo creditCardInfo);

    Integer applyBankCard(BankCardInfo bankCardInfo);

}
