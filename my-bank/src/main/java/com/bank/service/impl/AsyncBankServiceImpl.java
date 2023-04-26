package com.bank.service.impl;

import com.bank.entity.BankUserInfo;
import com.bank.entity.CreditCardInfo;
import com.bank.mapper.BankUserMapper;
import com.bank.mapper.CreditCardMapper;
import com.bank.service.AsyncBankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AsyncBankServiceImpl implements AsyncBankService {

    Logger log = LoggerFactory.getLogger(AsyncBankServiceImpl.class);

    @Autowired
    private BankUserMapper bankUserMapper;

    @Autowired
    private CreditCardMapper creditCardMapper;

    //手动事务管理
    @Autowired
    private TransactionDefinition transactionDefinition;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Override
    @Async("common")
    public CompletableFuture<Integer> registerUser(BankUserInfo bankUserInfo, CountDownLatch latchMain, CountDownLatch latch, AtomicInteger atomicInteger) {
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        Integer insert = null;
        try {
            log.info(Thread.currentThread().getName() + "running!");
            insert = bankUserMapper.registerUser(bankUserInfo);
            //模拟阻塞3秒
            //Thread.sleep(3000);

            latch.countDown();
            latchMain.await();
            if (atomicInteger.get() > 0) {
                transactionManager.rollback(transaction);
                log.info("子线程事务报错，开始回滚");
            } else {
                //手动提交
                transactionManager.commit(transaction);
            }

        } catch (Exception e) {
            log.info("子线程事务报错，开始回滚");
            atomicInteger.getAndIncrement();
            //手动回滚
            transactionManager.rollback(transaction);
            latch.countDown();
        }

        return CompletableFuture.completedFuture(insert);

        //return new AsyncResult<Integer>(insert);
    }

    @Override
    @Async("common")
    public CompletableFuture<Integer> applyCreditCard(CreditCardInfo creditCardInfo, CountDownLatch latchMain, CountDownLatch latch, AtomicInteger atomicInteger) {
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        Integer insert = null;
        try {
            log.info(Thread.currentThread().getName() + "running!");
            insert = creditCardMapper.applyCreditCard(creditCardInfo);
            //模拟阻塞3秒
            //Thread.sleep(3000);

            latch.countDown();
            latchMain.await();
            if (atomicInteger.get() > 0) {
                transactionManager.rollback(transaction);
                log.info("子线程事务报错，开始回滚");
            } else {
                //手动提交
                transactionManager.commit(transaction);
            }

        } catch (Exception e) {
            log.info("子线程事务报错，开始回滚");
            atomicInteger.getAndIncrement();
            //手动回滚
            transactionManager.rollback(transaction);
            latch.countDown();
        }

        return CompletableFuture.completedFuture(insert);

        //return new AsyncResult<>(insert);
    }

}
