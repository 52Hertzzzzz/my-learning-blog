package com.framework.handler.security;

import com.alibaba.fastjson.JSON;
import com.framework.utils.Result;
import com.framework.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        e.printStackTrace();
        Result<Object> error = Result.error(403, "无操作权限");
        WebUtils.renderString(httpServletResponse, JSON.toJSONString(error));
    }
}
