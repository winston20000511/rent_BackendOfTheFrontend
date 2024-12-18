package com.example.demo.helper;

// 自定義的 RuntimeException 異常類別
public class UnTokenException extends RuntimeException {

    // 帶錯誤訊息的建構子
    public UnTokenException(String message) {
        super(message);
    }
}

