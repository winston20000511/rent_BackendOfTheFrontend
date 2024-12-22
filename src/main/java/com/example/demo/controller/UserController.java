package com.example.demo.controller;

import com.example.demo.dto.UserCenterDTO;
import com.example.demo.dto.UserRegisterDTO;
import com.example.demo.dto.UserSimpleInfoDTO;
import com.example.demo.dto.UserUpdateDTO;
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
            /*
             * String token = JwtUtil.sign(user.getEmail(),user.getUserId(), user.getName())
             * response.put("name", user.getName());
             */
        	Map<String, Object> response = new HashMap<>();
            String token = JwtUtil.sign(user.getEmail(), user.getUserId(), user.getName());

            response.put("token", token);
            response.put("userId", user.getUserId());
            response.put("email", user.getEmail());
            response.put("name", user.getName());

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).body("帳號或密碼錯誤");
    }

/*
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
    @PostMapping("/userCenter")
    public ResponseEntity<UserCenterDTO> getUserCenterData(@RequestHeader("Authorization") String authorization) {
        log.info("收到獲取會員中心資料的請求");

        try {
            UserCenterDTO userCenterDTO = userService.getUserCenterData(authorization);
            return ResponseEntity.ok(userCenterDTO);
        } catch (RuntimeException e) {
            log.error("獲取會員中心資料失敗，原因: {}", e.getMessage());
            return ResponseEntity.status(401).body(null);
        }
    }

    /**
     * 會員簡歷查詢 API (POST 方法)
     *
     * @param authorization HTTP 標頭中的 JWT Token
     * @return 會員基本資料的 JSON 格式
     */
    @PostMapping("/userSimpleInfo")
    public ResponseEntity<UserSimpleInfoDTO> getUserSimpleInfo(@RequestHeader("Authorization") String authorization) {
        log.info("收到會員簡歷查詢請求");

        try {
            // 從 Service 層取得會員基本資料
            UserSimpleInfoDTO userSimpleInfo = userService.getUserSimpleInfo(authorization);
            return ResponseEntity.ok(userSimpleInfo); // 返回成功結果
        } catch (RuntimeException e) {
            log.error("會員簡歷查詢失敗，原因: {}", e.getMessage());
            return ResponseEntity.status(401).body(null); // 返回失敗結果
        }
    }

    /**
     * 更新會員資料的 API
     *
     * @param authorization JWT Token 用於驗證身份
     * @param updateRequest 更新資料的 JSON 格式
     * @return 更新後的會員中心資料
     */
    @PutMapping("/update")
    public ResponseEntity<UserCenterDTO> updateProfile(
            @RequestHeader("Authorization") String authorization,
            @RequestBody UserUpdateDTO updateRequest) {
        log.info("收到會員資料更新請求：{}", updateRequest);

        try {
            // 使用 JWT 驗證並更新會員資料
            UserCenterDTO updatedUser = userService.updateUserProfile(authorization, updateRequest);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            log.error("會員資料更新失敗，原因：{}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
}