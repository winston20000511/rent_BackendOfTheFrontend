package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetLink(String to, String resetLink) {
        // 建立 SimpleMailMessage 物件
        SimpleMailMessage message = new SimpleMailMessage();

        // 設定收件人
        message.setTo(to);

        // 設定標題
        message.setSubject("重設密碼");

        // 設定信件內容
        String emailContent = "Rent189會員您好，\n\n"
                + "您收到此封信，是因為我們接到您要求重設密碼的請求。\n\n"
                + "請點擊以下連結重設您的密碼：\n"
                + resetLink + "\n\n"
                + "如果您沒有進行此請求，請忽略此郵件。\n\n"
                + "謝謝，敬祝順心！\n\n"
                + "Rent189網站團隊";

        message.setText(emailContent);

        // 發送郵件
        mailSender.send(message);
    }
}
