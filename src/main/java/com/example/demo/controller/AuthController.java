package com.example.demo.controller;

import com.example.demo.model.UserTableBean;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.JwtUtils;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // 允許跨域
public class AuthController {

    @Autowired
    UserRepository userRepository;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> user) {
	    String email = user.get("email");
	    String password = user.get("password");

	    // 從資料庫查詢使用者

	    
	    UserTableBean dbUser = userRepository.findByEmail(email); // 用使用者輸入的 email 查詢

	    if (dbUser != null && BCrypt.checkpw(password, dbUser.getPassword())) {
	        // 如果驗證成功，生成 JWT 並返回
	        String token = JwtUtils.generateToken(email); // 使用從資料庫查詢到的 email
	        Map<String, String> response = new HashMap<>();
	        response.put("token", token);
	        response.put("email", dbUser.getEmail()); // 返回用戶 email，若需要
	        return ResponseEntity.ok(response);
	    }

	    // 驗證失敗
	    return ResponseEntity.status(401).body("Invalid credentials");
	}
}
