package com.uzdz.bpm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class SimpleActivitiStudyAllApplicationTests {

    @Test
    void contextLoads() {
        // 创建密码加密规则类
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        // 使用内存管理UserDetailsService
        UserDetailsService detailsService = new InMemoryUserDetailsManager();
        // 在内存中创建用户
        ((InMemoryUserDetailsManager) detailsService).createUser(User.withUsername("user").password(bCryptPasswordEncoder.encode("abc")).roles("ADMIN").build());

        // 使用Dao用户密码授权验证Provider
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // 设置Provider家在其验证时用于获取用户名对应的用户信息方式UserDetailsService
        provider.setUserDetailsService(detailsService);
        // 设置Provider校验密码时，AuthenticationToken密码加密规则
        provider.setPasswordEncoder(bCryptPasswordEncoder);

        // 创建AuthenticationManager，并将Provider设置进去
        List<AuthenticationProvider> providers = Arrays.asList(provider);
        AuthenticationManager authenticationManager = new ProviderManager(providers);

        // 创建一个AuthenticationToken交给AuthenticationManager进行授权，并返回Authentication认证凭证信息
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken("user", "abc");
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        System.out.println(authenticate);
    }

}
