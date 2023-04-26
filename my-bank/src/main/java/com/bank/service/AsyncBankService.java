package com.bank.service;

import com.bank.entity.BankUserInfo;
import com.bank.entity.CreditCardInfo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public interface AsyncBankService {

    CompletableFuture<Integer> registerUser(BankUserInfo bankUserInfo, CountDownLatch latchMain, CountDownLatch latch, AtomicInteger atomicInteger);

    CompletableFuture<Integer> applyCreditCard(CreditCardInfo creditCardInfo, CountDownLatch latchMain, CountDownLatch latch, AtomicInteger atomicInteger);

}
