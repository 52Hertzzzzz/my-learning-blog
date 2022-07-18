package com.blog.aspect;

import com.alibaba.fastjson.JSON;
import com.blog.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/***
 * 切面类：确定切点 + 通知方法（增强代码）
 */
@Component
@Aspect
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.blog.annotation.SystemLog)")
    public void a(){

    }

    //环绕通知，指定运用切点
    @Around("a()")
    public Object printLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object proceed;
        try {
            handleBefore(proceedingJoinPoint);
            proceed = proceedingJoinPoint.proceed();
            handleAfter(proceed);
        } finally {
            //系统换行符
            log.info("=======End=======" + System.lineSeparator());
        }
        return proceed;
    }

    private void handleAfter(Object proceed) {

        // 打印出参
        log.info("Response       : {}", JSON.toJSONString(proceed));

    }

    private void handleBefore(ProceedingJoinPoint proceedingJoinPoint) {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        //获取被增强方法上的注解对象
        SystemLog systemLog = getSystemLog(proceedingJoinPoint);

        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}", request.getRequestURL());
        // 打印描述信息
        log.info("BusinessName   : {}", systemLog.bussinessName());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}",
                proceedingJoinPoint.getSignature().getDeclaringTypeName(),
                ((MethodSignature) proceedingJoinPoint.getSignature()).getName());
        // 打印请求的 IP
        log.info("IP             : {}", request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}", JSON.toJSON(proceedingJoinPoint.getArgs()));
    }

    /***
     * joinPoint中含有被增强方法的各种参数（包括类名，方法名，参数值，返回值等）
     * 其中的Signature属性里有调用方法的各种属性
     * 我们需要调用方法中 SystemLog注解 中的属性，则要先获取该方法
     * Signature的实现类MethodSignature有获取Method的方法
     * @param proceedingJoinPoint
     * @return
     */
    private SystemLog getSystemLog(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        SystemLog annotation = signature.getMethod().getAnnotation(SystemLog.class);
        return annotation;
    }

}
