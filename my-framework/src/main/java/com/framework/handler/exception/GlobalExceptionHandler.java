package com.framework.handler.exception;

import com.framework.enums.AppHttpCodeEnum;
import com.framework.exception.BankException;
import com.framework.exception.SystemException;
import com.framework.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//增强Controller，或者用复合注解@RestControllerAdvice
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    //自定义处理异常，统一处理
    @ExceptionHandler(SystemException.class)
    public Result<?> systemExceptionHandler(SystemException e){
        //打印异常信息（用日志），加占位符
        log.error("SystemException出现了异常！{}", e);
        //从异常对象中获取提示信息，并封装返回响应给前端
        return Result.error(e.getCode(), e.getMsg());
    }

    //其他异常处理
    @ExceptionHandler(Exception.class)
    public Result<?> exceptionHandler(Exception e){
        //打印异常信息（用日志），加占位符
        log.error("Exception出现了异常！{}", e);
        //从异常对象中获取提示信息，并封装返回响应给前端
        return Result.error(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(BankException.class)
    public Result<?> bankExceptionHanlder(BankException e) {
        //打印异常信息（用日志），加占位符
        log.error("BankException出现了异常！{}", e);
        //从异常对象中获取提示信息，并封装返回响应给前端
        return Result.error(e.getCode(), e.getMsg());
    }

}
