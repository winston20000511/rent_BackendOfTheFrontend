package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.helper.JwtUtils;
import com.example.demo.model.UserTableBean;
import com.example.demo.repository.UserRepository;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class); // 初始化 logger

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> user) {
        logger.info("Login attempt with email: {}", user.get("email"));
        
        String email = user.get("email");
        String password = user.get("password");

        // 從資料庫查詢使用者
        UserTableBean dbUser = userRepository.findByEmail(email);
        //&& BCrypt.checkpw(password, dbUser.getPassword())
        if (dbUser != null ) {
            // 如果驗證成功，生成 JWT 並返回
            String token = JwtUtils.generateToken(dbUser.getEmail());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("email", dbUser.getEmail());
            return ResponseEntity.ok(response);
        }

        logger.warn("Invalid credentials for email: {}", email);
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
