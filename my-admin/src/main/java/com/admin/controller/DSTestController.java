package com.admin.controller;

import com.admin.service.DSTestService;
import com.framework.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ds")
public class DSTestController {

    @Autowired
    private DSTestService dsTestService;

    @GetMapping("/test1")
    public Result<?> test1() {
        String s = dsTestService.test1();
        return Result.ok(s);
    }

    @GetMapping("/test2")
    public Result<?> test2() {
        String s = dsTestService.test2();
        return Result.ok(s);
    }

    @GetMapping("/test3")
    public Result<?> test3() {
        String s = dsTestService.test3();
        return Result.ok(s);
    }

    @GetMapping("/test4")
    public Result<?> test4() {
        String s = dsTestService.test4();
        return Result.ok(s);
    }

}
