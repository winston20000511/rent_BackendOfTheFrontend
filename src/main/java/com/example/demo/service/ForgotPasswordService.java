package com.example.demo.service;

import com.example.demo.model.UserTableBean;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final EmailService emailService;;
    private final PasswordEncoder passwordEncoder;

    /**
     * 檢查電子郵件是否存在於資料庫
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * 處理忘記密碼的請求
     */
    public void processForgotPassword(String email) {
        // 檢查 Email 是否存在於資料庫
        if (!emailExists(email)) {
            log.info("不存在的 Email：{}", email);
            throw new IllegalArgumentException("該電子郵件不存在！");
        }

        // 如果 Email 存在於資料庫
        String token = generateResetToken();
        log.info("生成的重置 Token：{}", token);

        // 儲存 Token 到資料庫
        UserTableBean user = userRepository.findByEmail(email); // 假設已定義此方法
        if (user == null) {
            throw new IllegalArgumentException("使用者資料不存在！");
        }
        user.setResetToken(token); // 假設 UserTableBean 包含 resetToken 欄位
        userRepository.save(user); // 儲存更新的使用者資料
        log.info("重置 Token 已儲存到資料庫，用戶：{}", user.getEmail());

        // 生成前端 Vue Router 的重設密碼連結
        String resetLink = generateFrontendResetLink(token);
        log.info("生成的密碼重置連結：{}", resetLink);

        // 發送重設密碼連結
      sendResetLink(email, resetLink,user.getName());
        log.info("已發送密碼重置信件給 {}, 電子郵件地址為 {}", user.getName(), email);
    }

    private void sendResetLink(String email, String resetLink, String name) {
        emailService.sendPasswordResetEmail(email,resetLink,name);
    }

    /**
     * 生成前端 Vue Router 的重設密碼連結
     */
    private String generateFrontendResetLink(String token) {
        // 確保生成的連結只有純 token
        return token;
    }

    /**
     * 驗證重設密碼連結的 Token
     */
    public boolean validateToken(String token) {
        // 從資料庫驗證 Token
        UserTableBean user = userRepository.findByResetToken(token); // 假設已定義此方法
        if (user == null) {
            log.info("無效的 Token：{}", token);
            return false;
        }
        boolean isValid = token.equals(user.getResetToken());
        log.info("驗證結果：{}，Token：{}", isValid, token);
        return isValid;
    }

    /**
     * 生成唯一的密碼重設 Token
     */
    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    public boolean resetNewPassword(String token, String newPassword) {
        UserTableBean byResetToken = userRepository.findByResetToken(token);
        String encode = passwordEncoder.encode(newPassword);
        byResetToken.setPassword(encode);
        userRepository.save(byResetToken);
        log.info("使用者{}密碼變動成功",byResetToken.getName());
        return true;
    }
}