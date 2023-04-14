package com.blog.service;

import com.blog.vo.BlogUserLoginVo;
import com.framework.entity.User;
import com.framework.utils.Result;

public interface BlogLoginService {

    BlogUserLoginVo login(User user);

    Result<?> logout();

}
