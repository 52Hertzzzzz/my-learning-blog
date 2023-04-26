package com.bank.mapper;

import com.bank.entity.CreditCardInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface CreditCardMapper extends BaseMapper<CreditCardInfo> {

    Integer applyCreditCard(CreditCardInfo creditCardInfo);

}
