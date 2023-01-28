package com.framework.handler.security;

import com.alibaba.fastjson.JSON;
import com.framework.utils.Result;
import com.framework.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    //认证出错的相应json为security默认的异常处理结果
    //我们需要自定义异常处理，就需要实现该接口
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //包含两种异常：InsufficientAuthenticationException 需要登录
        //             BadCredentialsException 用户名或密码错误/
        //需要进行判断，响应不同的内容
        e.printStackTrace();
        Result<Object> error = null;
        if(e instanceof BadCredentialsException){
            error = Result.error(505, e.getMessage());
        }else if (e instanceof InsufficientAuthenticationException){
            error = Result.error(401, "请登录后重试");
        }else {
            error = Result.error(500, "认证或授权失败");
        }
        WebUtils.renderString(httpServletResponse, JSON.toJSONString(error));
    }
}
