package com.bank.service;

import com.bank.entity.BankCardInfo;
import com.bank.vo.BankRegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface BankRegisterService extends IService<BankCardInfo> {

    Integer applyBankCard(BankRegisterVo bankRegisterVo);

}
