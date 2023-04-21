package com.bank.controller;

import com.bank.entity.OrderInfo;
import com.bank.entity.StuffInfo;
import com.bank.service.OrderService;
import com.bank.vo.OrderInfoResponseVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.framework.utils.Result;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/pay")
public class OrderController {

    Logger log = LoggerFactory.getLogger(OrderController.class);

    @Resource
    private Validator validator;

    @Autowired
    private OrderService orderService;

    @GetMapping("/listStuffs")
    public Result<?> listStuffs(@RequestParam(required = false) String stuffName,
                                @RequestParam(required = false, defaultValue = "0") Integer currentPage,
                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        try {
            HashMap<String, Object> resultMap = Maps.newHashMapWithExpectedSize(2);
            Page<StuffInfo> page = new Page<>(currentPage, pageSize);
            IPage<StuffInfo> result = orderService.listStuffs(page, stuffName);

            resultMap.put("total", result.getTotal());
            resultMap.put("records", result.getRecords());
            return Result.ok(resultMap);
        } catch (Exception e) {
            return Result.error("商品查询失败");
        }
    }

    @PostMapping("/addStuffs")
    public Result<?> addStuffs(@RequestBody List<StuffInfo> stuffsList) {
        try {
            Integer num = orderService.addStuffs(stuffsList);

            return Result.ok("加入商品成功");
        } catch (Exception e) {
            log.error("加入商品失败");
            return Result.error("加入商品失败");
        }
    }

    @PostMapping("/addOrder")
    public Result<?> addOrder(@RequestBody OrderInfo orderInfo) {
        try {
            Result<?> result = orderService.addOrder(orderInfo);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("订单生成失败");
            return Result.error("订单生成失败");
        }
    }

    @GetMapping("/listOrders")
    public Result<?> listOrders(@RequestParam(value = "userName") String userName) {
        List<OrderInfoResponseVo> res = null;
        try {
            res = orderService.listOrders(userName);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询异常");
        }
        return Result.ok(res);
    }

    @GetMapping("/listOrders2")
    public Result<?> listOrders2(@RequestParam(value = "userName") String userName) {
        List<OrderInfoResponseVo> res = null;
        try {
            res = orderService.listOrders2(userName);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询异常");
        }
        return Result.ok(res);
    }


    @PostMapping("/payOrder")
    public Result<?> payOrder(@RequestBody OrderInfo orderInfo) {
        try {
            Integer i = orderService.payOrder(orderInfo);
            if (i == 0) {
                return Result.error("订单支付失败，请稍后再试");
            } else if (i == 1) {
                return Result.ok("您的账户余额不足");
            } else {
                return Result.ok("订单支付成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("订单支付失败");
        }
    }

    @GetMapping("/queryOrderDetail")
    public Result<?> queryOrderDetail() {
        return null;
    }

}
