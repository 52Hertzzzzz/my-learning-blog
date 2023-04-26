package com.framework.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.framework.utils.RedisUtil;
import com.framework.utils.Result;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/***
 * 支付相关接口幂等校验过滤器
 * 先仅用用户名进行下单限制，15s内只能下单一次（用户秒杀限制）
 *
 * 完整版幂等校验：
 * 1.前端发起支付请求前，先请求token，服务端返回token并写入redis
 * 2.前端发起支付请求时，携带token和支付数据
 * 3.服务端对token进行校验，如redis中存在对应token，则说明还未进行支付，进行支付逻辑，完成后删除token
 * 4.如redis不存在对应token，说明支付已完成，直接返回前端
 */
@Slf4j
@Component
public class WebInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.addHeader("Access-Control-Max-Age", "3600");
        response.setCharacterEncoding("UTF-8");
        // 获取访问url
        String url = request.getRequestURL().toString();
        log.info("请求URL: {}", url);

        ServletInputStream inputStream = request.getInputStream();
        ServletOutputStream outputStream = response.getOutputStream();
        BufferedReader reader = null;
        String line = null;
        StringBuilder param = new StringBuilder();
        // 或使用StreamUtils.copyToString(inputStream, "UTF-8");
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((line = reader.readLine()) != null) {
                param.append(line);
            }
        } catch (IOException e) {
            log.error("读取数据异常");
            Result<Object> error = Result.error("网络异常，请稍后再试");
            byte[] resultByte = JSONObject.toJSONString(error).getBytes("UTF-8");
            outputStream.write(resultByte);
            return false;
        }
        log.info("支付相关接口入参: {}", param.toString());

        //入参Json字符串解析
        JSONObject paramJson = JSONObject.parseObject(param.toString());
        String username = paramJson.getString("username");
        if (Strings.isNullOrEmpty(username)) {
            log.error("入参异常");
            Result<Object> error = Result.error("入参异常，请重新发起请求");
            byte[] resultByte = JSONObject.toJSONString(error).getBytes("UTF-8");
            outputStream.write(resultByte);
            return false;
        }

        String redisKey = "addOrderTimesConstraint:".concat(username);
        if (redisUtil.setNx(redisKey, "1", 15, TimeUnit.SECONDS)) {
            log.info("用户下单次数校验成功");
        } else {
            log.error("用户重复下单，直接拒绝");
            Result<Object> error = Result.error("请勿重复下单");
            byte[] resultByte = JSONObject.toJSONString(error).getBytes("UTF-8");
            outputStream.write(resultByte);
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
