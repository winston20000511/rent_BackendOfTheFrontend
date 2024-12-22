package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用於返回會員中心頁面的 DTO
 */
@Data
public class UserCenterDTO {
    private Long userId;         // 使用者 ID
    private String name;         // 使用者名稱
    private String email;        // 電子郵件
    private String phone;        // 手機號碼
    private byte[] picture;      // 使用者圖片
    private Byte gender;         // 性別（0: 男, 1: 女）
    private Byte coupon;         // 優惠券數量
    private Byte status;         // 帳號狀態（0: 停用, 1: 啟用）
    private LocalDateTime createTime; // 建立時間
}
