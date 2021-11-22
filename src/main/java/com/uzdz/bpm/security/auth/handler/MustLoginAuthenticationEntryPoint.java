package com.uzdz.bpm.security.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uzdz.bpm.base.Response;
import com.uzdz.bpm.base.exception.BaseErrorCodeEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 未授权处理
 */
@Component
public class MustLoginAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static ObjectMapper objectMapper = new ObjectMapper();

    // 身份验证入口,当需要登录却没登录时调用
    // 具体为,当抛出AccessDeniedException异常时且当前是匿名用户时调用
    // 匿名用户: 当过滤器链走到匿名过滤器(AnonymousAuthenticationFilter)时,
    // 会进行判断SecurityContext是否有凭证(Authentication),若前面的过滤器都没有提供凭证,
    // 匿名过滤器会给SecurityContext提供一个匿名的凭证(可以理解为用户名和权限为anonymous的Authentication),
    // 这也是JwtHeadFilter发现请求头中没有jwtToken不作处理而直接进入下一个过滤器的原因
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(Response.ofEnum(BaseErrorCodeEnum.NEED_LOGIN)));
    }
}
