package com.example.demo.controller;

import com.example.demo.helper.JwtUtil;
import com.example.demo.model.UserTableBean;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173") // 允許跨域
public class UserController {

    @Autowired
    private UserService userService;

    // 登入邏輯
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        UserTableBean user = userService.getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            // 驗證成功，生成 JWT
            String token = JwtUtil.sign(user.getEmail());
            
            // 返回 Token 和用戶基本資料
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", user.getUserId());
            response.put("email", user.getEmail());
            response.put("name", user.getName());

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).body("帳號或密碼錯誤");
    }
}
