package com.example.demo.service;

import com.example.demo.model.UserTableBean;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    // 檢查電子郵件是否存在於資料庫
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // 處理忘記密碼的請求
    public void processForgotPassword(String email) {
        // 檢查 Email 是否存在於資料庫
        if (!emailExists(email)) {
            log.info("不存在的 Email：{}", email);
            throw new IllegalArgumentException("該電子郵件不存在！");
        }

        // 如果 Email 存在於資料庫
        String token = generateResetToken();

        // 儲存 Token 到資料庫
        UserTableBean user = userRepository.findByEmail(email); // 假設已定義此方法
        user.setResetToken(token); // 假設 UserTableBean 包含 resetToken 欄位
        userRepository.save(user); // 儲存更新的使用者資料

        // 發送重設密碼連結
        emailService.sendResetLink(email, token);
        log.info("已發送密碼重置信件給 {}, 電子郵件地址為 {}", user.getName(), email);
    }

    // 驗證重設密碼連結的 Token
    public boolean validateToken(String token) {
        // 從資料庫驗證 Token
        UserTableBean user = userRepository.findByResetToken(token); // 假設已定義此方法
        return user != null && token.equals(user.getResetToken());
    }

    // 生成唯一的密碼重設 Token
    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }
}
