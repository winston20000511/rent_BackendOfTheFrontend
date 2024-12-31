package com.example.demo.controller;

import com.example.demo.config.GoogleConfig;
import com.example.demo.helper.JwtUtil;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@CrossOrigin(origins = "http://localhost:5173") // 允許跨域
@Slf4j
@Controller
@RequestMapping("/api/user")
public class GoogleOAuthController {

    @Autowired
    private GoogleConfig googleConfig;

    @Autowired
    private UserService userService;

    /**
     * 導向 Google OAuth 登入頁面
     */
    @GetMapping("/google/login")
    public String googleLogin() {
        String authUrl = "https://accounts.google.com/o/oauth2/v2/auth?" +
                "client_id=" + googleConfig.getClientId() +
                "&response_type=code" +
                "&scope=openid%20email%20profile" +
                "&redirect_uri=" + googleConfig.getRedirectUri() +
                "&state=state";
        return "redirect:" + authUrl;
    }

    /**
     * Google OAuth 回調處理
     */
    @GetMapping("/google/callback")
    public ResponseEntity<?> googleCallback(@RequestParam String code) {
        try {
            // 用 code 換取 Token
            RestTemplate restTemplate = new RestTemplate();
            String tokenUrl = "https://oauth2.googleapis.com/token";
            String queryBody = UriComponentsBuilder.newInstance()
                    .queryParam("code", code)
                    .queryParam("grant_type", "authorization_code")
                    .queryParam("client_id", googleConfig.getClientId())
                    .queryParam("client_secret", googleConfig.getClientSecret())
                    .queryParam("redirect_uri", googleConfig.getRedirectUri())
                    .build()
                    .toString();

            String credentials = restTemplate.postForObject(tokenUrl, null, String.class, queryBody);

            // 解析 Token
            JsonNode tokenJson = new ObjectMapper().readTree(credentials);
            String accessToken = tokenJson.get("access_token").asText();

            // 獲取用戶資訊
            String userInfoUrl = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + accessToken;
            String userInfo = restTemplate.getForObject(userInfoUrl, String.class);

            JsonNode userJson = new ObjectMapper().readTree(userInfo);
            String userEmail = userJson.get("email").asText();

            log.info("Google 登入成功，用戶 Email：{}", userEmail);

            // 驗證用戶是否存在
            String jwt = userService.handleGoogleLogin(userEmail);

            return ResponseEntity.ok(jwt); // 返回 JWT 給前端
        } catch (Exception e) {
            log.error("Google 登入失敗：{}", e.getMessage());
            return ResponseEntity.status(400).body("Google 登入失敗，請重試");
        }
    }
}