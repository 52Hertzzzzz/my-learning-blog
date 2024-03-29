package com.blog.service.impl;

import com.blog.service.BlogLoginService;
import com.blog.vo.BlogUserLoginVo;
import com.blog.vo.UserInfoVo;
import com.framework.entity.LoginUser;
import com.framework.entity.User;
import com.framework.utils.JwtUtil;
import com.framework.utils.RedisUtil;
import com.framework.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    //用于登录认证
    //该类不会自动注入，因此要自行用配置类（SecurityConfig）来注入
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisUtil redisUtil;

    //调用ProviderManager的方法进行认证，认证成功则生成jwt
    @Override
    public BlogUserLoginVo login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        //authenticationManager实际上会调用到UserDetailsService来进行用户认证
        //但默认的认证方法是在内存中查询，我们需要从数据库中查询
        //因此我们需要重写UserDetails的实现类，实现自定义认证方法
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userId，生成JWT
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入redis
        redisUtil.set("bloglogin:" + userId, loginUser);
        //把token和userInfo封装起来返回
        //先将User转成UserInfoVo
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(loginUser.getUser(), userInfoVo);
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt, userInfoVo);

        return blogUserLoginVo;
    }

    @Override
    public Result<?> logout() {
        //获取token并解析，获取userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();
        //删除redis中的用户信息
        redisUtil.del("bloglogin:" + userId);
        return Result.ok();
    }
}
