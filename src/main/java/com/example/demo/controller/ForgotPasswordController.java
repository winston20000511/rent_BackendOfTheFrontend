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

import java.util.Map;

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
    @PostMapping("/resetPassword")
    public ResponseEntity<String> validateResetToken(@RequestBody Map<String, String> resetData) {
        String token = resetData.get("token");
        String newPassword = resetData.get("newPassword");
        log.info("resetData {} {}", token, newPassword);

        if (forgotPasswordService.validateToken(token)) {
            if (forgotPasswordService.resetNewPassword(token,newPassword)){
                return ResponseEntity.ok("修改成功");
            }else {
                return ResponseEntity.badRequest().body("密碼設定失敗：");
            }
        } else {
            return ResponseEntity.badRequest().body("token驗證失敗："); // 返回錯誤
        }
    }
}
