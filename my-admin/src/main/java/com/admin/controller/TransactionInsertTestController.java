package com.admin.controller;

import com.admin.service.TransactionInsertTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/insertTest")
public class TransactionInsertTestController {

    @Autowired
    private TransactionInsertTestService service;

    @GetMapping("/test1")
    @Transactional(rollbackFor = Exception.class)
    public void test1() {
        service.insertMain();
        service.readMain();
        service.readSlave();
        service.readSlave1();
    }

    @GetMapping("/test2")
    @Transactional(rollbackFor = Exception.class)
    public void test2() {
        service.insertMain1();
        service.readMain();
        service.readSlave();
        service.readSlave1();
    }

}
