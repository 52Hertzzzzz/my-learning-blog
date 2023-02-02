package com.bank.controller;

import com.alibaba.excel.constant.ExcelXmlConstants;
import com.bank.entity.BankCardInfo;
import com.bank.service.BankRegisterService;
import com.bank.vo.BankRegisterVo;
import com.framework.utils.BeanValidators;
import com.framework.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Validator;

@RestController
@RequestMapping("/bankRegister")
public class BankRegisterController {

    @Autowired
    private Validator validator;

    @Autowired
    private BankRegisterService bankRegisterService;

    @PostMapping("/applyBankCard")
    public Result<?> applyBankCard(@RequestBody BankRegisterVo bankRegisterVo) {
        try {

            //TODO 并发事务处理
            //1.悲观锁 select for update
            //2.乐观锁 + 版本号
            //3.mysql自带原子操作 排它锁 update
            //4.使用原子类 AtomicInteger

            //参数校验
            //和@Valid不要同时使用，否则会失效，无法获取自定义异常
            BeanValidators.validateWithException(validator, bankRegisterVo);
            Integer integer = bankRegisterService.applyBankCard(bankRegisterVo);
            return Result.ok("银行卡办理成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }

    }

}
