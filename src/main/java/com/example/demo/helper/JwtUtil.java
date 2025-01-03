package com.example.demo.helper;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtUtil {

    // 設定過期時間為120分鐘（以毫秒為單位）
    private static final long EXPIRE_TIME = 120 * 60 * 1000; // 120分鐘
    // 定義密鑰，用於簽名和驗證JWT
    private static final String TOKEN_SECRET = "189tokenSecret";  //密鑰
    // Token字段名
    public static final String TOKEN = "authorization";

    // 簽名生成，根據用戶名生成JWT
    public static String sign(String userName , Long userId) {
        return JWT.create()
                // 設置主題，即用戶名
                .withSubject(userName)
                // 設置過期時間
                .withClaim("userId", userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                // 使用HMAC256算法和密鑰進行簽名
                .sign(Algorithm.HMAC256(TOKEN_SECRET));
    }

    // 驗證方法，驗證JWT並返回主題（用戶名）
    public static String[] verify(String token) {
        try {
            // 創建JWT驗證器
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).build();
            // 驗證token並解碼
            DecodedJWT jwt = verifier.verify(token);
            String userEmail = jwt.getSubject();
            Integer userId = jwt.getClaim("userId").asInt();
            // 返回主題（用戶名），如果jwt為null則返回null
            return jwt != null ? new String[]{userEmail,userId.toString()} : null;
        } catch (TokenExpiredException e) {
            // 捕獲token過期異常，並拋出自定義異常
            throw new UnTokenException("Token expired.");
        } catch (Exception e) {
            // 捕獲其他異常，並拋出自定義異常
            log.error(e.getMessage());
            throw new UnTokenException("Token error.");
        }
    }

    // 檢查token是否需要更新
    public static boolean isNeedUpdate(String token) {
        try {
            // 獲取token的過期時間
            Date expiresAt = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).build().verify(token).getExpiresAt();
            // 如果過期時間減去當前時間小於過期時間的一半，則需要更新
            return (expiresAt.getTime() - System.currentTimeMillis()) < (EXPIRE_TIME >> 1);
        } catch (TokenExpiredException e) {
            // 如果token已過期，則需要更新
            return true;
        } catch (Exception e) {
            // 捕獲其他異常，返回false表示不需要更新
            return false;
        }
    }

    // Email 驗證 Token 的過期時間為 6 小時
    private static final long EMAIL_EXPIRE_TIME = 6 * 60 * 60 * 1000; // 6小時

    // 生成 Email 驗證專用的 JWT
    public static String generateEmailVerificationToken(String userEmail, Long userId) {
        return JWT.create()
                .withSubject(userEmail)
                .withClaim("userId", userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + EMAIL_EXPIRE_TIME))
                .sign(Algorithm.HMAC256(TOKEN_SECRET));
    }

    // 驗證 Email 驗證專用 Token
    public static String[] verifyEmailToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).build();
            DecodedJWT jwt = verifier.verify(token);
            String userEmail = jwt.getSubject();
            Integer userId = jwt.getClaim("userId").asInt();
            return jwt != null ? new String[]{userEmail, userId.toString()} : null;
        } catch (TokenExpiredException e) {
            throw new UnTokenException("Email 驗證 Token 已過期，請重新請求驗證信。");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UnTokenException("Email 驗證 Token 無效。");
        }
    }

}