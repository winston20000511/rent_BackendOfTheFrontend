package com.example.demo.controller;

import com.example.demo.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    // 忘記密碼請求處理
    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody String email) {
        try {
            forgotPasswordService.processForgotPassword(email);
            return ResponseEntity.ok("重設密碼連結已寄出至您的電子信箱。");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("發送失敗：" + e.getMessage());
        }
    }

    // 驗證重設連結的 token
    @GetMapping("/resetPassword")
    public ResponseEntity<String> validateResetToken(@RequestParam("token") String token) {
        if (forgotPasswordService.validateToken(token)) {
            return ResponseEntity.ok("Token 驗證成功，請繼續設定新密碼。");
        } else {
            return ResponseEntity.badRequest().body("Token 無效或已過期。");
        }
    }
}


