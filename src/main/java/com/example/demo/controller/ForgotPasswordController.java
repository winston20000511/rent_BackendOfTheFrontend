package com.example.demo.controller;

import com.example.demo.helper.Public;
import com.example.demo.request.ForgotPasswordRequest;
import com.example.demo.service.EmailService;
import com.example.demo.service.ForgotPasswordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @Public
    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody @Validated ForgotPasswordRequest forgotPasswordRequest) {
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
    public ResponseEntity<String> validateResetToken(@RequestParam("token") String token) {
        if (forgotPasswordService.validateToken(token)) {
            return ResponseEntity.ok("Token 驗證成功，請繼續設定新密碼。");
        } else {
            return ResponseEntity.badRequest().body("Token 無效或已過期。");
        }
    }
}


