package com.uzdz.bpm.security;

import com.uzdz.bpm.security.filter.JwtAuthenticationFilter;
import com.uzdz.bpm.security.filter.JwtHeadAuthenticationFilter;
import com.uzdz.bpm.security.auth.SimpleAuthenticationProvider;
import com.uzdz.bpm.security.auth.SimpleUserDetailsService;
import com.uzdz.bpm.security.auth.handler.AuthenticationFailureHandler;
import com.uzdz.bpm.security.auth.handler.AuthenticationSuccessHandler;
import com.uzdz.bpm.security.auth.handler.MustLoginAuthenticationEntryPoint;
import com.uzdz.bpm.security.auth.handler.NoAuthorityAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private SimpleUserDetailsService simpleUserDetailsService;

    @Autowired
    private MustLoginAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private NoAuthorityAuthenticationEntryPoint noAuthorityAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 登录过滤器
        JwtAuthenticationFilter jwtLoginFilter = new JwtAuthenticationFilter();
        jwtLoginFilter.setAuthenticationManager(this.authenticationManagerBean());

        // 登录成功和失败的操作
        jwtLoginFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        jwtLoginFilter.setAuthenticationFailureHandler(authenticationFailureHandler);

        // 登录过滤器的授权提供者(就这么叫吧)
        SimpleAuthenticationProvider provider = new SimpleAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(simpleUserDetailsService);

        // JWT校验过滤器（即如果当前已授权，则跳过AuthenticationFilter）
        JwtHeadAuthenticationFilter headFilter = new JwtHeadAuthenticationFilter(this.authenticationManager(), simpleUserDetailsService);

        http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(noAuthorityAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/", "/publicMsg", "/login").permitAll()
                .antMatchers("/innerMsg").authenticated()
                .anyRequest().access("@simpleAccessDecisionService.hasPermission(request , authentication)")
                .and()
                // 将授权提供者注册到授权管理器中(AuthenticationManager)
                .authenticationProvider(provider)
                .addFilterAfter(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(headFilter, JwtAuthenticationFilter.class)
                //禁用session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 跨域
                .csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }
}
