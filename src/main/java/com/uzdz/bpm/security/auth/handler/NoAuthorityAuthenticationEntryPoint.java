package com.uzdz.bpm.security.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uzdz.bpm.base.Response;
import com.uzdz.bpm.base.exception.BaseErrorCodeEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限不足
 */
@Component
public class NoAuthorityAuthenticationEntryPoint implements AccessDeniedHandler {

    private static ObjectMapper objectMapper = new ObjectMapper();

    //拒绝访问处理,当已登录,但权限不足时调用
    //抛出AccessDeniedException异常时且当不是匿名用户时调用
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(Response.ofEnum(BaseErrorCodeEnum.INSUFFICIENT_PERMISSIONS)));
    }
}
