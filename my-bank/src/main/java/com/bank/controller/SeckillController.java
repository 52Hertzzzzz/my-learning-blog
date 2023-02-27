package com.bank.controller;

import com.bank.entity.StuffInfo;
import com.bank.service.SeckillService;
import com.framework.entity.SeckillOrderInfo;
import com.framework.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    @PostMapping("/seckillStuff")
    public Result<?> seckillStuff(@RequestBody SeckillOrderInfo orderInfo) {
        int i = seckillService.seckillStuff(orderInfo);

        return i > 0 ? Result.ok("购买成功") : Result.error("该商品已经被抢完啦");
    }

}
