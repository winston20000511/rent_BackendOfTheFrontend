package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用於會員簡歷頁面的 DTO
 */
@Data
public class UserSimpleInfoDTO {
    private String name;         // 使用者名稱
    private String email;        // 電子郵件
    private String phone;        // 手機號碼
    private String picture;      // 使用者圖片

}
