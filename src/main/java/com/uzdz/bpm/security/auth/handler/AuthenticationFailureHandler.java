package com.uzdz.bpm.security.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uzdz.bpm.base.Response;
import com.uzdz.bpm.base.exception.BaseErrorCodeEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权认证失败
 */
@Component
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(Response.ofEnum(BaseErrorCodeEnum.PASSWORD_ERROR)));
    }
}