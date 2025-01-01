package com.example.demo.dto;

import lombok.Data;

/**
 * 用於接收會員資料更新請求的 DTO
 */
@Data
public class UserUpdateDTO {
    private String name;         // 使用者名稱
    private String phone;        // 手機號碼
    private Byte gender;         // 性別（0: 男, 1: 女）
    private String password;     // 密碼
    private String picture;      // 使用者圖片（Base64 格式）
}
