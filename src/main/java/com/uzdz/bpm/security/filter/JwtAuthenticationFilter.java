package com.uzdz.bpm.security.filter;

import com.uzdz.bpm.security.auth.domain.SimpleLoginToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public JwtAuthenticationFilter() {
        super(new AntPathRequestMatcher("/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            String userName = request.getParameter("userName");
            String password = request.getParameter("password");
            //创建未认证的凭证(etAuthenticated(false)),注意此时凭证中的主体principal为用户名
            SimpleLoginToken jwtLoginToken = new SimpleLoginToken(userName, password);
            //将认证详情(ip,sessionId)写到凭证
            jwtLoginToken.setDetails(new WebAuthenticationDetails(request));
            //AuthenticationManager获取受支持的AuthenticationProvider(这里也就是SimpleAuthenticationProvider),
            //生成已认证的凭证,此时凭证中的主体为userDetails
            Authentication token = this.getAuthenticationManager().authenticate(jwtLoginToken);
            return token;
        } catch (Exception e) {
            log.error("用户认证失败：" + e.getMessage(), e);
            throw new BadCredentialsException("Bad credentials");
        }
    }
}
