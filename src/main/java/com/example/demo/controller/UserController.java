package com.example.demo.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import com.example.demo.model.UserTableBean;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    // 註冊使用者的功能
    @PostMapping("/register")
    public ResponseEntity<String> registerUser( @RequestBody UserTableBean user) {
        // 呼叫 Service 來儲存使用者資料
        boolean isRegistered = userService.registerUser(user);
        
        if (isRegistered) {
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.status(400).body("User registration failed");
        }
    }

    // 根據 email 查詢使用者
    @GetMapping("/email/{email}")
    public ResponseEntity<String> getUserByEmail(@PathVariable String email, String username, String password, HttpSession httpSession, Model model) {
        Optional<UserTableBean> user = userService.getUserByEmail(email);
        
        if (user.isPresent()) {
            UserTableBean result = userService.checkLogin(username, password);
            
            if (result != null) {
                httpSession.setAttribute("loginUserId", result.getUserId());
                httpSession.setAttribute("loginUsername", result.getName());
                httpSession.setAttribute("loginUserEmail", result.getEmail());
                
                return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/")).build();  // Redirect to homepage
            } else {
                model.addAttribute("errorMsg", "帳密錯誤");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("帳密錯誤");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("用戶未找到");
        }
    }}

		
		
		
	
	

