package com.bank.controller;

import com.bank.service.BankRegisterService;
import com.bank.vo.BankRegisterVo;
import com.framework.enums.AppHttpCodeEnum;
import com.framework.utils.BeanValidators;
import com.framework.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Validator;

@RestController
@RequestMapping("/bankRegister")
public class BankController {

    @Autowired
    private Validator validator;

    @Autowired
    private BankRegisterService bankRegisterService;

    @PostMapping("/applyBankCard")
    public Result<?> applyBankCard(@RequestBody BankRegisterVo bankRegisterVo) {
        try {

            //Todo:并发事务处理
            //1.悲观锁 select for update
            //2.乐观锁 + 版本号
            //3.mysql自带原子操作 排它锁 update
            //4.使用原子类 AtomicInteger

            //参数校验
            //不要与@Valid同时使用，否则会与工具类冲突，无法获取自定义异常
            BeanValidators.validateWithException(validator, bankRegisterVo);
            Integer integer = bankRegisterService.applyBankCard(bankRegisterVo);

            if (integer.equals(AppHttpCodeEnum.BANK_REGISTER_SUCCESS.getCode())) {
                return Result.ok("银行卡办理成功");
            } else {
                return Result.error("银行卡办理失败，请稍后再试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("银行卡办理失败，请稍后再试");
        }
    }

}
