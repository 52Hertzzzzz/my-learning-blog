package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.mapper.UserMapper;
import com.framework.entity.LoginUser;
import com.framework.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    //Resource是用name注入，即bean的id属性
    //会在Bean中寻找“userMapper”对应的实现类
    //Autowired是用type注入，即bean的class中，接口对应的实现类
    //会在Bean中寻找“UserMapper”对应的类
    @Resource
    private UserMapper userMapper;

    //重写该方法，自定义验证用户的方法
    //注意：该方法的返回值是一个接口，接口虽然不能被实例化，但接口实现类都可以上向转型为接口
    //在运行时只需修改实现类类型，就可以实现不同的功能，而不必要修改接口的代码，目的是解耦
    //因此我们需要组装一个UserDetails的实现类来做返回值
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(queryWrapper);
        //2.判断是否查到用户，如未查到则抛出异常
        if (Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }
        //3.返回用户信息
        LoginUser loginUser = new LoginUser(user);
        return loginUser;
    }
}
