package com.example.demo.service;

import com.example.demo.model.UserTableBean;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ForgotPasswordService {

    // 檢查電子郵件是否存在於資料庫
    private final UserRepository userRepository;
    private final EmailService emailService;

    public boolean emailExists(String email) {
        // TODO: 使用 Repository 檢查資料庫
        return userRepository.existsByEmail(email); // 範例邏輯
    }



    // 處理忘記密碼的請求
    public void processForgotPassword(String email) {
        //檢查Email是否存在在資料庫
        if (!emailExists(email)) {
            log.info("不存在的email{}",email);
            throw new IllegalArgumentException("該電子郵件不存在！");
        }
        //如果email存在在資料庫中
        String token = generateResetToken();
        // TODO: 儲存 token 到資料庫，並寄送重設密碼連結
        emailService.sendResetLink(email, token);
        UserTableBean userTableBean = new UserTableBean();

        log.info("已發送密碼重置信件給{},信箱地址為{}",userTableBean.getName(),email);

    }

    // 驗證重設密碼連結的 Token
    public boolean validateToken(String token) {
        // TODO: 從資料庫驗證 Token
        return token.equals("mock-valid-token"); // 範例邏輯
    }

    // 生成唯一的密碼重設 Token
    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }
}

