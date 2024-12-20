package com.example.demo.controller;

import com.example.demo.dto.UserCenterDTO;
import com.example.demo.dto.UserRegisterDTO;
import com.example.demo.helper.JwtUtil;
import com.example.demo.model.UserTableBean;
import com.example.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * RESTful API 控制層，處理使用者相關的 HTTP 請求
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173") // 允許跨域
@Slf4j
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
            String token = JwtUtil.sign(user.getEmail() , user.getUserId());

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



    /**
     * 使用者註冊 API
     *
     * @param userRegisterDTO 註冊資料
     * @return 註冊結果
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("收到註冊請求：{}", userRegisterDTO);
        String result = userService.registerUser(userRegisterDTO);
        if ("註冊成功".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 獲取會員中心資料
     *
     * @param authorization HTTP 標頭中的 JWT Token
     * @return 會員中心資料的 JSON
     */
    @GetMapping("/userCenter")
    public ResponseEntity<UserCenterDTO> getUserCenterData(@RequestHeader("Authorization") String authorization) {
        log.info("收到獲取會員中心資料的請求");

        try {
            // 提取 Bearer Token 這裡要看前端那邊怎麼寫，104 105八成要改
            String token = authorization.replace("Bearer ", "");
            UserCenterDTO userCenterDTO = userService.getUserCenterData(authorization);
            return ResponseEntity.ok(userCenterDTO);
        } catch (RuntimeException e) {
            log.error("獲取會員中心資料失敗，原因: {}", e.getMessage());
            return ResponseEntity.status(401).body(null);
        }
    }
}