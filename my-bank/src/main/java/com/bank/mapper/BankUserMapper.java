package com.bank.mapper;

import com.bank.entity.BankUserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface BankUserMapper extends BaseMapper<BankUserInfo> {

    Integer registerUser(BankUserInfo bankUserInfo);

}
