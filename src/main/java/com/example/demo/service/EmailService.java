package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 發送密碼重設郵件
     *
     * @param to 收件者郵箱
     * @param resetLink 密碼重設連結
     * @param userName 使用者名稱
     */
    public void sendPasswordResetEmail(String to, String resetLink, String userName) {
        String subject = "密碼重設通知";
        String text = "親愛的 " + userName + "，\n\n"
                + "我們收到您重設密碼的請求。\n"
                + "請點擊以下連結重設您的密碼（連結將在 30 分鐘內失效）：\n\n"
                + resetLink + "\n\n"
                + "如果您未請求重設密碼，請忽略此郵件。\n\n"
                + "感謝使用我們的服務！";

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);

            log.info("密碼重設郵件已發送至：{}", to);
        } catch (Exception e) {
            log.error("發送密碼重設郵件失敗：{}", e.getMessage());
            throw new RuntimeException("無法發送密碼重設郵件，請稍後再試");
        }
    }
}
