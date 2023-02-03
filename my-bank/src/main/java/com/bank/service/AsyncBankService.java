package com.bank.service;

import com.bank.entity.BankUserInfo;
import com.bank.entity.CreditCardInfo;

import java.util.concurrent.CompletableFuture;

public interface AsyncBankService {

    CompletableFuture<Integer> registerUser(BankUserInfo bankUserInfo);

    CompletableFuture<Integer> applyCreditCard(CreditCardInfo creditCardInfo);

}
