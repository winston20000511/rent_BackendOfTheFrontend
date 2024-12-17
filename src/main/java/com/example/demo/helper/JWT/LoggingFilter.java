package com.example.demo.helper.JWT;

import java.io.IOException;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoggingFilter extends OncePerRequestFilter {

	    @Override
	    protected void doFilterInternal(@NonNull HttpServletRequest request,
	    								@NonNull HttpServletResponse response,
	    								@NonNull FilterChain filterChain)
	            throws ServletException, IOException {
	        long startTime = System.currentTimeMillis();
	        filterChain.doFilter(request, response);
	        long duration = System.currentTimeMillis() - startTime;

	        System.out.println("Request to: " + request.getRequestURI() + 
	                           " | Method: " + request.getMethod() +
	                           " | Duration: " + duration + " ms");
	    }
	}

