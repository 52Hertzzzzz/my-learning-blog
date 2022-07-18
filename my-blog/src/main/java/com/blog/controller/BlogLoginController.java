package com.blog.controller;

import com.blog.entity.User;
import com.blog.enums.AppHttpCodeEnum;
import com.blog.exception.SystemException;
import com.blog.service.BlogLoginService;
import com.blog.utils.Result;
import com.blog.vo.BlogUserLoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/***
 * 登录相关功能
 */
@RestController
public class BlogLoginController {

    @Autowired
    private BlogLoginService blogLoginService;

    //自定义登录接口
    @PostMapping("/login")
    public Result<?> login(@RequestBody User user){
        if (!StringUtils.hasText(user.getUserName())){
            //提示 必须输入用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        BlogUserLoginVo login = blogLoginService.login(user);
        return Result.ok(login);
    }


    @PostMapping("/logout")
    public Result<?> logout(){
        Result<?> logout = blogLoginService.logout();
        return logout;
    }

}
