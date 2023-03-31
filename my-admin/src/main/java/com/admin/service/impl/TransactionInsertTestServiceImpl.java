package com.admin.service.impl;

import com.admin.mapper.TransactionInsertTestMapper;
import com.admin.service.TransactionInsertTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TransactionInsertTestServiceImpl implements TransactionInsertTestService {

    private static AtomicInteger integer = new AtomicInteger(0);

    @Autowired
    private TransactionInsertTestMapper mapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertMain() {
        mapper.insertMain(integer.incrementAndGet());
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NOT_SUPPORTED)
    public void insertMain1() {
        mapper.insertMain(integer.incrementAndGet());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void readMain() {
        int i = mapper.readMain();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NOT_SUPPORTED)
    public void readSlave() {
        int i = mapper.readSlave();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NOT_SUPPORTED)
    public void readSlave1() {
        int i = mapper.readMain();
    }

}
