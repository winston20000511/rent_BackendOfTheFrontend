package com.example.demo.helper.JWT;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.helper.JWT.JwtFilter;

@Configuration
public class SecurityConfig {
	
    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // 禁用 CSRF 保護
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll() 
                .anyRequest().authenticated() // 其他請求需驗證
            )
            .sessionManagement(session -> session
                .maximumSessions(1) // 限制同一用戶只能有一個 Session
            );

        // 添加 JWT 過濾器
        http.addFilterAfter(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

