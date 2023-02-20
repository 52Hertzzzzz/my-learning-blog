package com.framework.filter;

import com.alibaba.fastjson.JSON;
import com.framework.entity.LoginUser;
import com.framework.utils.JwtUtil;
import com.framework.utils.RedisUtil;
import com.framework.utils.Result;
import com.framework.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtil RedisUtil;

    //认证过滤器实现
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头中的token
        String token = httpServletRequest.getHeader("token");
        if(!StringUtils.hasText(token)){
            //说明该接口不需要登录，直接放行
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        //解析获取userid
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            //token超时或token非法
            //响应前端需要重新登陆
            //由于filter不由SpringMVC管理，返回值不会被默认转换为json格式，需要自行转换
            //先封装成Result对象，再封装成json
            Result<Object> error = Result.error(401, "请您登录后重试");
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(error));
            return;
        }
        String userId = claims.getSubject();
        //从redis中获取对应用户信息
        LoginUser cacheObject = (LoginUser) RedisUtil.get("bloglogin:" + userId);

        if (Objects.isNull(cacheObject)){
            //未找到用户信息，说明登录过期
            Result<Object> error = Result.error(401, "请您登录后重试");
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(error));
            return;
        }
        //存入SecurityContextHolder
        //由于是已认证状态，所以需要使用3参数构造器，则传入3个参数：账号+密码+权限
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(cacheObject, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
