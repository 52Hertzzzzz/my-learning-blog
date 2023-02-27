package com.bank.service;

import com.bank.entity.OrderInfo;
import com.bank.entity.StuffInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.framework.utils.Result;

import java.util.List;

public interface OrderService {

    IPage<StuffInfo> listStuffs(Page<StuffInfo> page, String stuffName);

    Integer addStuffs(List<StuffInfo> stuffsList);

    Result<?> addOrder(OrderInfo orderInfo);

}
