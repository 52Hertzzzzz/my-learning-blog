package com.blog.handler.exception;

import com.blog.enums.AppHttpCodeEnum;
import com.blog.exception.SystemException;
import com.blog.utils.Result;
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
        log.error("出现了异常！{}", e);
        //从异常对象中获取提示信息，并封装返回响应给前端
        return Result.error(e.getCode(), e.getMsg());
    }

    //其他异常处理
    @ExceptionHandler(Exception.class)
    public Result<?> exceptionHandler(Exception e){
        //打印异常信息（用日志），加占位符
        log.error("出现了异常！{}", e);
        //从异常对象中获取提示信息，并封装返回响应给前端
        return Result.error(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }

}
