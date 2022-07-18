package com.blog.service;

import com.blog.entity.User;
import com.blog.utils.Result;
import com.blog.vo.BlogUserLoginVo;

public interface BlogLoginService {

    BlogUserLoginVo login(User user);

    Result<?> logout();

}
