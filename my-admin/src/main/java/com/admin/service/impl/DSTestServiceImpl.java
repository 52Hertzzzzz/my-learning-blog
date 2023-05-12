package com.admin.service.impl;

import com.admin.mapper.DSTestMapper;
import com.admin.service.DSTestService;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableAspectJAutoProxy(exposeProxy = true)
public class DSTestServiceImpl implements DSTestService {

    @Autowired
    private DSTestMapper dsTestMapper;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public String test1() {
        String m1 = dsTestMapper.m1();
        String m2 = dsTestMapper.m2();
        String m3 = dsTestMapper.m3();
        StringBuilder builder = new StringBuilder(m1);
        builder.append(" | ");
        builder.append(m2);
        builder.append(" | ");
        builder.append(m3);
        return builder.toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @DS("master")
    public String test2() {
        String m1 = dsTestMapper.m1();
        return m1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public String test3() {
        DSTestServiceImpl proxy = (DSTestServiceImpl) AopContext.currentProxy();
        String m1 = dsTestMapper.m1();
        String m2 = proxy.test2();

        StringBuilder builder = new StringBuilder(m1);
        builder.append(" | ");
        builder.append(m2);
        return builder.toString();
    }

    @Override
    @DS("slave")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public String test4() {
        DSTestServiceImpl proxy = (DSTestServiceImpl) AopContext.currentProxy();
        String m1 = dsTestMapper.m1();
        String m2 = proxy.test2();

        StringBuilder builder = new StringBuilder(m1);
        builder.append(" | ");
        builder.append(m2);
        return builder.toString();
    }

}
