package com.example.demo.dto;

import lombok.Data;

/**
 * 資料傳輸物件，負責接收前端註冊請求的數據
 */
@Data
public class UserRegisterDTO {
    private String name;       // 使用者名稱
    private String email;      // 電子郵件
    private String password;   // 密碼
    private String phone;      // 手機號碼
    private Byte gender;       // 性別（0: 男, 1: 女）
}
