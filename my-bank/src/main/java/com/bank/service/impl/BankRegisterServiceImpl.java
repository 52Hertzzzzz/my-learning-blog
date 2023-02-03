package com.bank.service.impl;

import com.bank.entity.BankCardInfo;
import com.bank.entity.BankUserInfo;
import com.bank.entity.CreditCardInfo;
import com.bank.mapper.BankCardMapper;
import com.bank.mapper.BankUserMapper;
import com.bank.mapper.CreditCardMapper;
import com.bank.service.AsyncBankService;
import com.bank.service.BankRegisterService;
import com.bank.vo.BankRegisterVo;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
public class BankRegisterServiceImpl extends ServiceImpl<BankCardMapper, BankCardInfo> implements BankRegisterService {

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private BankUserMapper bankUserMapper;

    @Autowired
    private CreditCardMapper creditCardMapper;

    @Autowired
    private AsyncBankService asyncBankService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer applyBankCard(BankRegisterVo bankRegisterVo) throws InterruptedException {
        String UUID = IdWorker.get32UUID().substring(16);
        bankRegisterVo.setBankCardNum(UUID);

        BankCardInfo bankCardInfo = new BankCardInfo();
        BankUserInfo bankUserInfo = new BankUserInfo();
        CreditCardInfo creditCardInfo = new CreditCardInfo();
        BeanUtils.copyProperties(bankRegisterVo, bankCardInfo);
        BeanUtils.copyProperties(bankRegisterVo, bankUserInfo);
        BeanUtils.copyProperties(bankRegisterVo, creditCardInfo);
        creditCardInfo.setMoneyLimit(10000.00);

        //主线程执行，办理银行卡
        Thread.sleep(5000);
        int insert = bankCardMapper.applyBankCard(bankCardInfo);

        //会导致死锁!!!
        //Thread.currentThread().join();

        //子线程执行，申请会员 + 信用卡办理
        CompletableFuture<Integer> future = asyncBankService.registerUser(bankUserInfo);
        CompletableFuture<Integer> future1 = asyncBankService.applyCreditCard(creditCardInfo);
        CompletableFuture.allOf(future, future1);
        //Integer insert1 = future.join();
        //Integer insert2 = future1.join();
        //HashMap<String, Object> objectObjectHashMap = Maps.newHashMapWithExpectedSize(6);

        return insert;
    }

}
