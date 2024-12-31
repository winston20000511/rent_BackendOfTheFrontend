package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.request.ForgotPasswordRequest;
import com.example.demo.service.EmailService;
import com.example.demo.service.ForgotPasswordService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/forgot")
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordService forgotPasswordService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JavaMailSender mailSender;

    // 忘記密碼請求處理

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        log.info(forgotPasswordRequest.getEmail()
        );
        try {
            forgotPasswordService.processForgotPassword(forgotPasswordRequest.getEmail());
            return ResponseEntity.ok("重設密碼連結已寄出至您的電子信箱。");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("發送失敗：" + e.getMessage());
        }
    }

    // 驗證重設連結的 token
    @PutMapping("/resetPassword")
    public ResponseEntity<Void> validateResetToken(@RequestParam("token") String token) {
        if (forgotPasswordService.validateToken(token)) {
            // 驗證成功，重導向到 Vue 的 ResetPassword 頁面
            return ResponseEntity.status(302)
                    .header("Location", "http://localhost:8080/#/reset-password?token=" + token)
                    .build();
        } else {
            return ResponseEntity.badRequest().build(); // 返回錯誤
        }
    }
}