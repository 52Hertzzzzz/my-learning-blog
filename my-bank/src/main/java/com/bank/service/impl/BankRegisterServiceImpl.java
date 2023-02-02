package com.bank.service.impl;

import com.bank.entity.BankCardInfo;
import com.bank.entity.BankUserInfo;
import com.bank.entity.CreditCardInfo;
import com.bank.mapper.BankCardMapper;
import com.bank.mapper.BankUserMapper;
import com.bank.mapper.CreditCardMapper;
import com.bank.service.BankRegisterService;
import com.bank.vo.BankRegisterVo;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class BankRegisterServiceImpl extends ServiceImpl<BankCardMapper, BankCardInfo> implements BankRegisterService {

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private BankUserMapper bankUserMapper;

    @Autowired
    private CreditCardMapper creditCardMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Integer applyBankCard(BankRegisterVo bankRegisterVo) throws ExecutionException, InterruptedException {
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
        int insert = bankCardMapper.applyBankCard(bankCardInfo);
        int insert1 = bankUserMapper.registerUser(bankUserInfo);
        int insert2 = creditCardMapper.applyCreditCard(creditCardInfo);

        //子线程执行，申请会员 + 信用卡办理
        //Future<Integer> future1 = registerUser(bankUserInfo);
        //Future<Integer> future2 = applyCreditCard(creditCardInfo);

        return insert;
    }

    @Async("common")
    Future<Integer> registerUser(BankUserInfo bankUserInfo) {
        Integer insert = bankUserMapper.registerUser(bankUserInfo);
        try {
            //模拟阻塞3秒
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>(insert);
    }

    @Async("common")
    Future<Integer> applyCreditCard(CreditCardInfo creditCardInfo) {
        Integer insert = creditCardMapper.applyCreditCard(creditCardInfo);
        try {
            //模拟阻塞3秒
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>(insert);
    }

}
