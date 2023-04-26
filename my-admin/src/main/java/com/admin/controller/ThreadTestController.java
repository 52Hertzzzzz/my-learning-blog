package com.admin.controller;

import com.framework.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/threadTest")
@Slf4j
public class ThreadTestController {

    private static final ExecutorService executor = Executors.newFixedThreadPool(5);

    private static ReentrantLock lock = new ReentrantLock(true);

    @GetMapping("/test1")
    public synchronized Result<?> test1(@RequestParam(required = true) String userName) {
        String s = null;
        try {
            log.info("{} get Lock.", userName);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("hello, 我是");
            stringBuilder.append(userName);
            stringBuilder.append(", 我正在运行...");
            s = stringBuilder.toString();

            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return Result.ok(s);
    }

    @GetMapping("/test2")
    public Result<?> test2(@RequestParam(required = true) String userName) {
        String s = null;
        try {
            lock.lock();
            log.info("{} get Lock.", userName);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("hello, 我是");
            stringBuilder.append(userName);
            stringBuilder.append(", 我正在运行...");
            s = stringBuilder.toString();

            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            log.info("{} release Lock.", userName);
        }

        return Result.ok(s);
    }

    @GetMapping("/test3")
    public Result<?> test3(@RequestParam(required = true) String userName) {
        log.info("Test3 Start");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Runnable runnable = () -> {
            int i = 3;
            while (i > 0) {
                log.info("Thread Running...");
                try {
                    Thread.sleep(500);
                    i--;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

        log.info("Test3 Complete");
        return Result.ok("ok");
    }

}
