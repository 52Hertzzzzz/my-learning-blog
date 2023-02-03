package com.bank.service.impl;

import com.bank.entity.BankUserInfo;
import com.bank.entity.CreditCardInfo;
import com.bank.mapper.BankUserMapper;
import com.bank.mapper.CreditCardMapper;
import com.bank.service.AsyncBankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncBankServiceImpl implements AsyncBankService {

    Logger log = LoggerFactory.getLogger(AsyncBankServiceImpl.class);

    @Autowired
    private BankUserMapper bankUserMapper;

    @Autowired
    private CreditCardMapper creditCardMapper;

    @Override
    @Async("common")
    public CompletableFuture<Integer> registerUser(BankUserInfo bankUserInfo) {
        Integer insert = bankUserMapper.registerUser(bankUserInfo);
        try {
            log.info(Thread.currentThread().getName() + "running!");
            //模拟阻塞3秒
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(insert);

        //return new AsyncResult<Integer>(insert);
    }

    @Override
    @Async("common")
    public CompletableFuture<Integer> applyCreditCard(CreditCardInfo creditCardInfo) {
        Integer insert = creditCardMapper.applyCreditCard(creditCardInfo);
        try {
            log.info(Thread.currentThread().getName() + "running!");
            //模拟阻塞3秒
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(insert);

        //return new AsyncResult<>(insert);
    }

}
