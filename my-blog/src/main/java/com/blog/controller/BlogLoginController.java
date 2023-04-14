package com.blog.controller;

import com.blog.service.BlogLoginService;
import com.blog.vo.BlogUserLoginVo;
import com.framework.entity.User;
import com.framework.enums.AppHttpCodeEnum;
import com.framework.exception.SystemException;
import com.framework.utils.Result;
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

    /***
     * 登录接口
     * @param user
     * @return
     */
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

    /***
     * 退出登录
     * @return
     */
    @PostMapping("/logout")
    public Result<?> logout(){
        Result<?> logout = blogLoginService.logout();
        return logout;
    }

}
