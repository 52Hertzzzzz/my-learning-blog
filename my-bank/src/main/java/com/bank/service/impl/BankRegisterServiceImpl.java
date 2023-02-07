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
import com.framework.exception.BankException;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BankRegisterServiceImpl extends ServiceImpl<BankCardMapper, BankCardInfo> implements BankRegisterService {

    Logger log = LoggerFactory.getLogger(BankRegisterServiceImpl.class);

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private BankUserMapper bankUserMapper;

    @Autowired
    private CreditCardMapper creditCardMapper;

    @Autowired
    private AsyncBankService asyncBankService;

    //手动事务管理
    @Autowired
    private TransactionDefinition transactionDefinition;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Override
    //手动管理事务
    //@Transactional(rollbackFor = Exception.class)
    public Integer applyBankCard(BankRegisterVo bankRegisterVo) throws InterruptedException {
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        CountDownLatch latchMain = new CountDownLatch(1);
        CountDownLatch latch = new CountDownLatch(2);
        try {
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
            //子线程执行，申请会员 + 信用卡办理
            CompletableFuture<Integer> future = asyncBankService.registerUser(bankUserInfo, latchMain, latch, atomicInteger);
            CompletableFuture<Integer> future1 = asyncBankService.applyCreditCard(creditCardInfo, latchMain, latch, atomicInteger);
            //CompletableFuture.allOf(future, future1);
            //Integer insert1 = future.join();
            //Integer insert2 = future1.join();
            //HashMap<String, Object> objectObjectHashMap = Maps.newHashMapWithExpectedSize(6);

            latch.await();
            latchMain.countDown();
            //latchMain.await();
            if (atomicInteger.get() > 0) {
                transactionManager.rollback(transaction);
                log.info("子线程事务报错，开始回滚");
                throw new BankException(500, "子线程事务报错，开始回滚");
            } else {
                //手动提交
                transactionManager.commit(transaction);
            }
        } catch (Exception e) {
            atomicInteger.getAndIncrement();
            //手动回滚
            transactionManager.rollback(transaction);
            log.info("主线程事务报错，开始回滚");
            latchMain.countDown();
            throw new BankException(500, "主线程事务报错，开始回滚");
        }

        return null;
    }

}
