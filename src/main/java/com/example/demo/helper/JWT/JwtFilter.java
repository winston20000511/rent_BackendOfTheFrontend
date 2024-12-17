package com.example.demo.helper.JWT;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class JwtFilter extends OncePerRequestFilter {
	// use jakarta
		@Override
		protected void doFilterInternal(
				@NonNull HttpServletRequest request, 
				@NonNull HttpServletResponse response,
				@NonNull FilterChain filterChain) throws ServletException, IOException {

			String authHeader = request.getHeader("Authorization");

			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				String jwtToken = authHeader.substring(7);
				try {
					String username = JwtUtil.extractUsername(jwtToken);
					System.out.println("JWT 驗證成功, 用戶: " + username);
				} catch (Exception e) {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return;
				}
			}

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
