package com.admin.controller;

import com.admin.mapper.TransactionTestMapper;
import com.admin.service.TransactionTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactionTest")
public class TransactionTestController {

    @Autowired
    private TransactionTestService testService;

    @Autowired
    private TransactionTestMapper testMapper;

    @GetMapping("/test1")
    @Transactional(rollbackFor = Exception.class)
    public void transaction1() {
        testMapper.test1();
        testMapper.test2();
    }

    @GetMapping("/test2")
    public void transaction2() {
        testMapper.test1();
        testMapper.test2();
    }

    @GetMapping("/test3")
    public void transaction3() {
        testService.transaction1();
        testService.transaction2();
    }

    @GetMapping("/test4")
    @Transactional(rollbackFor = Exception.class)
    public void transaction4() {
        testService.transaction1();
        testService.transaction2();
    }

    @GetMapping("/test7")
    public void transaction7() {
        testService.transaction3();
        testService.transaction4();
    }

    @GetMapping("/test8")
    @Transactional(rollbackFor = Exception.class)
    public void transaction8() {
        testService.transaction3();
        testService.transaction4();
    }

    @GetMapping("/test9")
    public void transaction9() {
        testService.transaction4();
        testService.transaction5();
    }

    @GetMapping("/test12")
    public void transaction12() {
        testService.transaction2();
        testService.transaction6();
    }

    @GetMapping("/test5")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void transaction5() {
        testService.transaction1();
        testService.transaction2();
    }

    @GetMapping("/test6")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NOT_SUPPORTED)
    public void transaction6() {
        testService.transaction1();
        testService.transaction2();
    }

    @GetMapping("/test10")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void transaction10() {
        testService.transaction4();
        testService.transaction5();
    }

    @GetMapping("/test11")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NOT_SUPPORTED)
    public void transaction11() {
        testService.transaction4();
        testService.transaction5();
    }

    @GetMapping("/test13")
    public void transaction13() {
        testService.transaction6();
        testService.transaction7();
    }

    @GetMapping("/test14")
    @Transactional(rollbackFor = Exception.class)
    public void transaction14() {
        testService.transaction6();
        testService.transaction7();
    }

    @GetMapping("/test15")
    @Transactional(rollbackFor = Exception.class)
    public void transaction15() {
        testService.transaction8();
        testService.transaction9();
    }

}
