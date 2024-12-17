package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserRegisterDTO;
import com.example.demo.helper.JWT.JwtUtil;
import com.example.demo.model.UserTableBean;
import com.example.demo.service.AuthenticateService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class AuthenticateController {

    @Autowired
    private AuthenticateService authService;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO registerDTO) {
        
        authService.registerUser(registerDTO);
        return ResponseEntity.ok("註冊成功");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
    		@RequestBody Map<String, String> credentials, 
    		HttpSession session) {
    	
    	String email = credentials.get("email");
        String password = credentials.get("password");
        
        UserTableBean user = authService.validateUser(email, password);
        if (user != null) {
        	UserDTO claimsDTO = new UserDTO(
                    user.getUserId(),
                    user.getName(),
                    user.getEmail(),
                    user.getGender(),
                    user.getCoupon(),
                    user.getStatus()
            );
            
            String jwtToken = JwtUtil.generateToken(claimsDTO);
            
            Map<String, Object> userSessionData = new HashMap<>();
            userSessionData.put("userId", user.getUserId());
            userSessionData.put("name", user.getName());
            userSessionData.put("email", user.getEmail());
            userSessionData.put("phone", user.getPhone());
            userSessionData.put("picture", user.getPicture());
            userSessionData.put("createTime", user.getCreateTime());
            userSessionData.put("gender", user.getGender());
            userSessionData.put("coupon", user.getCoupon());
            userSessionData.put("status", user.getStatus());

            session.setAttribute("user", userSessionData);
            session.setAttribute("jwtToken", jwtToken);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "登入成功");
            response.put("jwtToken", jwtToken);
            response.put("user", userSessionData);

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).body("帳號或密碼錯誤");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
    	authService.sendResetPasswordEmail(email);
        return ResponseEntity.ok("已發送重置密碼郵件");
    }
}