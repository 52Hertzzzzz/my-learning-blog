package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.vo.UserInfoVo;
import com.framework.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2022-07-16 15:50:00
 */
public interface UserService extends IService<User> {

    UserInfoVo userInfo();

    Boolean updateUserInfo(User user);

    Boolean register(User user);
}
