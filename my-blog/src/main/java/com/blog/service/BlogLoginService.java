package com.blog.service;

import com.framework.entity.User;
import com.framework.utils.Result;
import com.blog.vo.BlogUserLoginVo;

public interface BlogLoginService {

    BlogUserLoginVo login(User user);

    Result<?> logout();

}
