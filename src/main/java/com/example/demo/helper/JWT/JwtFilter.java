package com.example.demo.helper.JWT;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class JwtFilter extends OncePerRequestFilter {
	// use jakarta
	
	private static final Key SECRET_KEY = JwtUtil.getSecretKey();;
	
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);
            try {
                // 解析 JWT Token
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(jwtToken)
                        .getBody();

                String username = claims.getSubject();
                System.out.println("JWT 驗證成功, 用戶: " + username);

                // 設置 Authentication 到 SecurityContext
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                System.out.println("Authorization Header: " + authHeader);
                System.out.println("Token: " + jwtToken);
                
            } catch (Exception e) {
                System.out.println("JWT 驗證失敗: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        // 繼續處理其他 Filter
        filterChain.doFilter(request, response);
    }

	/* use javax */
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String jwtToken = authHeader.substring(7);
//            try {
//                String username = JwtUtil.extractUsername(jwtToken);
//                System.out.println("JWT 驗證成功, 用戶: " + username);
//            } catch (Exception e) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return;
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }

	
}
