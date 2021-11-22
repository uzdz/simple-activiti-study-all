package com.uzdz.bpm.security.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uzdz.bpm.base.Response;
import com.uzdz.bpm.utils.JwtUtil;
import com.uzdz.bpm.security.auth.domain.SimpleUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权认证成功
 */
@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        SimpleUserDetails simpleUserDetails = (SimpleUserDetails) authentication.getPrincipal();
        String username = simpleUserDetails.getUsername();
        String token = JwtUtil.createToken(username);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(Response.getInstance(token)));
    }
}
