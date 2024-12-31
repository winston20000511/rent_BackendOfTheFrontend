package com.example.demo.controller;

import com.example.demo.config.GoogleConfig;
import com.example.demo.helper.JwtUtil;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173") // 允許跨域
@Slf4j
@Controller
@RequestMapping("/api/user")
public class GoogleOAuthController {

    @Autowired
    private GoogleConfig googleConfig;

    @Autowired
    private UserService userService;
    @Autowired
    private TreeCodec treeCodec;

    /**
     * 導向 Google OAuth 登入頁面
     */
    @ResponseBody
    @GetMapping("/google/login")
    public Map<String, String> googleLogin() {
        String authUrl = "https://accounts.google.com/o/oauth2/v2/auth?" +
                "client_id=" + googleConfig.getClientId() +
                "&response_type=code" +
                "&scope=openid%20email%20profile" +
                "&redirect_uri=" + googleConfig.getRedirectUri() +
                "&state=state";

        Map<String,String> response = new HashMap<>();
        response.put("authUrl", authUrl);
        return response;
    }

    /**
     * Google OAuth 回調處理
     */
    @GetMapping("/google/callback")
    public ResponseEntity<?> googleCallback(@RequestParam String code) throws JsonProcessingException {
        try {
            RestClient restClient=RestClient.create();

            // 用backend 做一般form送出的request
            String queryBody = UriComponentsBuilder.newInstance()
                    .queryParam("code", code)
                    .queryParam("grant_type", "authorization_code")
                    .queryParam("client_id", googleConfig.getClientId())
                    .queryParam("client_secret", googleConfig.getClientSecret())
                    .queryParam("redirect_uri",googleConfig.getRedirectUri())
                    .build()
                    .getQuery();

            String credentials = restClient.post()
                    .uri("https://oauth2.googleapis.com/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(queryBody)
                    .retrieve()
                    .body(String.class);

            System.out.println("credentials" + credentials);

            JsonNode jsonNode = new ObjectMapper().readTree(credentials);
            String accessToken = jsonNode.get("access_token").asText();
            String idToken = jsonNode.get("id_token").asText();

            String result = restClient.get()
                    .uri("https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + accessToken )
                    .header("Authorization", "Bearer " + idToken)
                    .retrieve()
                    .body(String.class);

            System.out.println("result: " + result);

            // 解析 result 拿到帳號
            JsonNode jsonNode2 = new ObjectMapper().readTree(result);
            String userEmail = jsonNode2.get("email").asText();

            System.out.println("userEmail:" + userEmail);
            //驗證用戶是否存在
            String jwt = userService.handleGoogleLogin(userEmail);
            return ResponseEntity.ok(jwt); // 返回 JWT 給前端
        }catch (Exception e) {
            log.error("Google 登入失敗：{}", e.getMessage());
            return ResponseEntity.status(400).body("Google 登入失敗，請重試");
        }

    }
//        try {
//            // 用 code 換取 Token
//            RestTemplate restTemplate = new RestTemplate();
//            String tokenUrl = "https://oauth2.googleapis.com/token";
//            String queryBody = UriComponentsBuilder.newInstance()
//                    .queryParam("code", code)
//                    .queryParam("grant_type", "authorization_code")
//                    .queryParam("client_id", googleConfig.getClientId())
//                    .queryParam("client_secret", googleConfig.getClientSecret())
//                    .queryParam("redirect_uri", googleConfig.getRedirectUri())
//                    .build()
//                    .toString();
//
//            String credentials = restTemplate.postForObject(tokenUrl, null, String.class, queryBody);
//
//            // 解析 Token
//            JsonNode tokenJson = new ObjectMapper().readTree(credentials);
//            String accessToken = tokenJson.get("access_token").asText();
//
//            // 獲取用戶資訊
//            String userInfoUrl = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + accessToken;
//            String userInfo = restTemplate.getForObject(userInfoUrl, String.class);
//
//            JsonNode userJson = new ObjectMapper().readTree(userInfo);
//            String userEmail = userJson.get("email").asText();
//
//            log.info("Google 登入成功，用戶 Email：{}", userEmail);
//
//            // 驗證用戶是否存在
//            String jwt = userService.handleGoogleLogin(userEmail);
//
//            return ResponseEntity.ok(jwt); // 返回 JWT 給前端
//        } catch (Exception e) {
//            log.error("Google 登入失敗：{}", e.getMessage());
//            return ResponseEntity.status(400).body("Google 登入失敗，請重試");
//        }
//    }
}