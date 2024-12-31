package com.example.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * 負責 Email 發送的服務層
 */
@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 發送驗證信
     *
     * @param toEmail   收件人 Email
     * @param userName  用戶名稱
     * @param verifyLink 驗證連結
     */
    public void sendVerificationEmail(String toEmail, String userName, String verifyLink) {
        try {
            // 創建 MimeMessage 用於發送 HTML 格式的郵件
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // 設定收件人、主題、內容
            helper.setTo(toEmail);
            helper.setSubject("189租屋網 - Email 驗證");
            helper.setFrom("rent189.customer.service@gmail.com");

            // 信件內容（HTML 格式）
            String emailContent = "<!DOCTYPE html>\r\n"
                    + "<html lang=\"zh-TW\">\r\n"
                    + "<head>\r\n"
                    + "  <meta charset=\"UTF-8\">\r\n"
                    + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
                    + "  <title>189租屋網 Email 驗證</title>\r\n"
                    + "  <style>\r\n"
                    + "    body {\r\n"
                    + "      font-family: Arial, sans-serif;\r\n"
                    + "      margin: 0;\r\n"
                    + "      padding: 0;\r\n"
                    + "      background-color: #f8f8f8;\r\n"
                    + "    }\r\n"
                    + "    .container {\r\n"
                    + "      width: 100%;\r\n"
                    + "      max-width: 640px;\r\n"
                    + "      margin: 0 auto;\r\n"
                    + "      background: #ffffff;\r\n"
                    + "      border-radius: 8px;\r\n"
                    + "      overflow: hidden;\r\n"
                    + "      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\r\n"
                    + "    }\r\n"
                    + "    .header {\r\n"
                    + "      background-color: #1e3a8a;\r\n"
                    + "      color: white;\r\n"
                    + "      text-align: center;\r\n"
                    + "      padding: 20px 0;\r\n"
                    + "    }\r\n"
                    + "    .header h1 {\r\n"
                    + "      font-size: 24px;\r\n"
                    + "      margin: 0;\r\n"
                    + "    }\r\n"
                    + "    .header p {\r\n"
                    + "      font-size: 14px;\r\n"
                    + "      margin-top: 8px;\r\n"
                    + "    }\r\n"
                    + "    .content {\r\n"
                    + "      padding: 32px;\r\n"
                    + "      border-left: 1px solid #e5e7eb;\r\n"
                    + "      border-right: 1px solid #e5e7eb;\r\n"
                    + "      border-bottom: 1px solid #e5e7eb;\r\n"
                    + "    }\r\n"
                    + "    .content h2 {\r\n"
                    + "      font-size: 18px;\r\n"
                    + "      margin-bottom: 16px;\r\n"
                    + "    }\r\n"
                    + "    .content p {\r\n"
                    + "      margin-bottom: 24px;\r\n"
                    + "      color: #4b5563;\r\n"
                    + "    }\r\n"
                    + "    .content .button {\r\n"
                    + "      text-align: center;\r\n"
                    + "      margin: 32px 0;\r\n"
                    + "    }\r\n"
                    + "    .content .button a {\r\n"
                    + "      background-color: #1e3a8a;\r\n"
                    + "      color: white;\r\n"
                    + "      text-decoration: none;\r\n"
                    + "      padding: 12px 32px;\r\n"
                    + "      border-radius: 4px;\r\n"
                    + "      display: inline-block;\r\n"
                    + "      font-size: 16px;\r\n"
                    + "      font-weight: 500;\r\n"
                    + "    }\r\n"
                    + "    .content .button a:hover {\r\n"
                    + "      background-color: #2563eb;\r\n"
                    + "    }\r\n"
                    + "    .content .alert {\r\n"
                    + "      background-color: #f9fafb;\r\n"
                    + "      padding: 16px;\r\n"
                    + "      border-radius: 4px;\r\n"
                    + "      margin-bottom: 24px;\r\n"
                    + "      color: #6b7280;\r\n"
                    + "    }\r\n"
                    + "    .content .alert ul {\r\n"
                    + "      padding-left: 20px;\r\n"
                    + "      margin: 8px 0;\r\n"
                    + "    }\r\n"
                    + "    .content .alert ul li {\r\n"
                    + "      list-style: disc;\r\n"
                    + "      margin-bottom: 8px;\r\n"
                    + "    }\r\n"
                    + "    .content .link {\r\n"
                    + "      font-size: 14px;\r\n"
                    + "      color: #1d4ed8;\r\n"
                    + "      word-break: break-all;\r\n"
                    + "    }\r\n"
                    + "    .footer {\r\n"
                    + "      text-align: center;\r\n"
                    + "      border-top: 1px solid #e5e7eb;\r\n"
                    + "      padding: 24px 16px;\r\n"
                    + "      margin-top: 32px;\r\n"
                    + "      font-size: 14px;\r\n"
                    + "      color: #6b7280;\r\n"
                    + "    }\r\n"
                    + "    .footer p {\r\n"
                    + "      margin: 4px 0;\r\n"
                    + "    }\r\n"
                    + "  </style>\r\n"
                    + "</head>\r\n"
                    + "<body>\r\n"
                    + "  <div class=\"container\">\r\n"
                    + "    <!-- 頂部 Logo 區域 -->\r\n"
                    + "    <div class=\"header\">\r\n"
                    + "      <h1>189租屋網</h1>\r\n"
                    + "      <p>Email 驗證</p>\r\n"
                    + "    </div>\r\n"
                    + "\r\n"
                    + "    <!-- 主要內容區域 -->\r\n"
                    + "    <div class=\"content\">\r\n"
                    + "      <h2>親愛的"+userName+"，您好：</h2>\r\n"
                    + "      <p>感謝您註冊 189租屋網！為了確保您的帳戶安全，請點擊下方按鈕驗證您的電子郵件地址。</p>\r\n"
                    + "\r\n"
                    + "      <!-- 驗證按鈕 -->\r\n"
                    + "      <div class=\"button\">\r\n"
                    + "        <a href=\""+verifyLink+"\">驗證電子郵件</a>\r\n"
                    + "      </div>\r\n"
                    + "\r\n"
                    + "      <!-- 安全提醒 -->\r\n"
                    + "      <div class=\"alert\">\r\n"
                    + "        <p>⚠️ 安全提醒 ⚠️</p>\r\n"
                    + "        <ul>\r\n"
                    + "          <li>如果這不是您的操作，請忽略此郵件</li>\r\n"
                    + "          <li>請勿將此郵件轉寄給他人</li>\r\n"
                    + "          <li>驗證連結將於 6小時 後失效</li>\r\n"
                    + "        </ul>\r\n"
                    + "      </div>\r\n"
                    + "\r\n"
                    + "      <!-- 備用驗證連結 -->\r\n"
                    + "      <p>如果按鈕無法點擊，請複製下方連結至瀏覽器：</p>\r\n"
                    + "      <p class=\"link\">"+verifyLink+"</p>\r\n"
                    + "    </div>\r\n"
                    + "\r\n"
                    + "    <!-- 聯絡資訊 -->\r\n"
                    + "    <div class=\"footer\">\r\n"
                    + "      <p>如有任何問題，請聯絡客服：rent189.customer.service@gmail.com</p>\r\n"
                    + "      <p>© 2024 189租屋網 版權所有</p>\r\n"
                    + "    </div>\r\n"
                    + "  </div>\r\n"
                    + "</body>\r\n"
                    + "</html>\r\n"
                    + "\"\"\"".formatted(userName, verifyLink, verifyLink);

            helper.setText(emailContent, true); // 第二個參數設定為 true 表示內容為 HTML 格式

            // 發送郵件
            mailSender.send(message);
            log.info("驗證信已成功發送至：{}", toEmail);
        } catch (MessagingException e) {
            log.error("發送驗證信時發生錯誤：{}", e.getMessage());
            throw new RuntimeException("Email 發送失敗，請稍後再試");
        }
    }
    
    
    /**
    
    發送重設密碼連結*
    @param to    收件人電子郵件
    @param token 密碼重設的唯一標誌 (Token)*/
    public void sendResetLink(String to, String token) {
        String baseUrl = "http://localhost:5173/reset-password/"; // Vue Router 配置已經是 history 模式Vue 前端頁面

            // 如果 token 已包含 baseUrl，則直接使用 token
            String resetLink = baseUrl + "?token=" + token;
            
            System.out.println("Reset Link: " + resetLink);

            // 構建電子郵件內容
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
