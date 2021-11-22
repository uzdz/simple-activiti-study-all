package com.uzdz.bpm.security.filter;


import com.uzdz.bpm.security.auth.SimpleUserDetailsService;
import com.uzdz.bpm.security.auth.domain.SimpleLoginToken;
import com.uzdz.bpm.utils.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtHeadAuthenticationFilter extends BasicAuthenticationFilter {

    private SimpleUserDetailsService simpleUserDetailsService;

    public JwtHeadAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    public JwtHeadAuthenticationFilter(AuthenticationManager authenticationManager, SimpleUserDetailsService simpleUserDetailsService) {
        super(authenticationManager);
        this.simpleUserDetailsService = simpleUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("Authentication");
        if (StringUtils.isNotBlank(token)) {
            try {
                String username = JwtUtil.extractUsername(token);
                if (StringUtils.isNoneBlank(username) && null == SecurityContextHolder.getContext().getAuthentication()) {
                    UserDetails userDetails = simpleUserDetailsService.loadUserByUsername(username);
                    SimpleLoginToken authRequest = new SimpleLoginToken(userDetails, null, userDetails.getAuthorities());
                    authRequest.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authRequest);
                }
            } catch (Exception e) {
                //recover
            }
        }

        chain.doFilter(request, response);
    }
}
