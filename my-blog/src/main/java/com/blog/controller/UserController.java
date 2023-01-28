package com.blog.controller;

import com.framework.annotation.SystemLog;
import com.framework.entity.User;
import com.blog.service.UserService;
import com.framework.utils.Result;
import com.blog.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /***
     * 获取用户信息
     * @return
     */
    @GetMapping("/userInfo")
    public Result<?> userInfo(){
        UserInfoVo userInfoVo = userService.userInfo();
        return Result.ok(userInfoVo);
    }

    /***
     * 修改个人信息
     * @param user
     * @return
     */
    @PutMapping("/userInfo")
    @SystemLog(bussinessName = "修改用户信息")
    public Result<?> updateUserInfo(@RequestBody User user){
        Boolean aBoolean = userService.updateUserInfo(user);
        return Result.ok(aBoolean);
    }

    /***
     * 注册新用户
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user){
        Boolean register = userService.register(user);
        return Result.ok(register);
    }

}
