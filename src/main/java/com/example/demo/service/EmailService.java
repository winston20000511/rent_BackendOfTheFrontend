package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 發送重設密碼連結
     *
     * @param to    收件人電子郵件
     * @param token 重設密碼的唯一標誌 (Token)
     */
    public void sendResetLink(String to, String token) {
        // 使用前端頁面提供的固定路徑
        String baseUrl = "http://localhost:8080/api/user/resetPassword"; // 前端的重設密碼頁面 URL
        String resetLink = baseUrl + "?token=" + token; // 拼接完整連結

        // 設定信件內容
        String emailContent = "Rent189會員您好，\n\n"
                + "我們收到您要求重設密碼的請求。\n\n"
                + "請點擊以下連結來重設您的密碼：\n"
                + resetLink + "\n\n"
                + "如果您未提出此請求，請忽略此郵件。\n\n"
                + "此連結有效期限為24小時，逾期將無效。\n\n"
                + "謝謝，祝您順心！\n\n"
                + "Rent189網站團隊";

        // 建立 SimpleMailMessage
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to); // 設定收件人
        message.setSubject("Rent189 - 重設密碼"); // 設定信件標題
        message.setText(emailContent); // 設定信件內容

        // 發送郵件
        mailSender.send(message);
    }
}
