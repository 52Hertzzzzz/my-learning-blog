package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.entity.User;
import com.blog.enums.AppHttpCodeEnum;
import com.blog.exception.SystemException;
import com.blog.mapper.UserMapper;
import com.blog.service.UserService;
import com.blog.utils.SecurityUtils;
import com.blog.vo.UserInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-07-16 15:50:00
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserInfoVo userInfo() {
        //获取当前用户ID
        Long userId = SecurityUtils.getUserId();
        //根据用户ID查询用户信息
        User user = getById(userId);
        //封装返回
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(user, userInfoVo);
        return userInfoVo;
    }

    @Override
    public Boolean updateUserInfo(User user) {
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.set("avatar", user.getAvatar())
                .set("email", user.getEmail())
                .set("nick_name", user.getNickName())
                .set("sex", user.getSex());
        userUpdateWrapper.eq("id", user.getId());
        boolean update = update(userUpdateWrapper);

        return update;
    }

    @Override
    public Boolean register(User user) {
        //数据非空校验 不为null/""
        if (!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        //数据重复判断（用户名，邮箱等）
        if (userNameExist(user.getUserName()) > 0){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExist(user.getNickName()) > 0){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //明文密码加密处理
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        boolean save = save(user);
        return save;
    }

    private Integer nickNameExist(String nickName) {
        Integer count = userMapper.nickNameExist(nickName);
        return count;
    }

    private Integer userNameExist(String userName) {
        Integer count = userMapper.userNameExist(userName);
        return count;
    }
}
