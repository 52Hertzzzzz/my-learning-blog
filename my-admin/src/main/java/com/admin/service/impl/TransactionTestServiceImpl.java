package com.admin.service.impl;

import com.admin.mapper.TransactionTestMapper;
import com.admin.service.TransactionTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionTestServiceImpl implements TransactionTestService {

    @Autowired
    private TransactionTestMapper testMapper;

    @Override
    public void transaction1() {
        testMapper.test1();
    }

    @Override
    public void transaction2() {
        testMapper.test2();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transaction3() {
        testMapper.test1();
        testMapper.test2();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transaction4() {
        testMapper.test1();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transaction5() {
        testMapper.test2();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void transaction6() {
        testMapper.test1();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void transaction7() {
        testMapper.test2();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NOT_SUPPORTED)
    public void transaction8() {
        testMapper.test1();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NOT_SUPPORTED)
    public void transaction9() {
        testMapper.test2();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NOT_SUPPORTED)
    public void transaction10(String s) {
        if ("1".equals(s)) {
            testMapper.test1();
        } else {
            testMapper.test2();
        }
    }

}
