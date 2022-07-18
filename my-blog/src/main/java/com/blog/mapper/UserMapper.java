package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.entity.User;
import org.apache.ibatis.annotations.Param;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-14 18:25:56
 */
public interface UserMapper extends BaseMapper<User> {

    Integer userNameExist(@Param("userName") String userName);

    Integer nickNameExist(@Param("nickName")String nickName);
}
