package com.example.demo.controller;

import com.example.demo.config.GoogleConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@CrossOrigin(origins = "http://localhost:5173") // 允許跨域
@Slf4j
@Controller
@RequestMapping("/api/user")
public class GoogleOAuthController {
	
	@Autowired
	private GoogleConfig googleConfig;
	
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
	
	@GetMapping("/google")
	public String googleCallback(@RequestParam String code) throws JsonMappingException, JsonProcessingException {
		
		RestClient restClient = RestClient.create();
		
		// 用 backend 做一般 form 送出的 request
		String queryBody = UriComponentsBuilder.newInstance()
		.queryParam("code", code)
		.queryParam("grant_type", "authorization_code")
		.queryParam("client_id", googleConfig.getClientId())
		.queryParam("client_secret", googleConfig.getClientSecret())
		.queryParam("redirect_uri", googleConfig.getRedirectUri())
		.build()
		.getQuery();
		
		// 用 code 交換 token 回來
		String credentials = restClient.post()
		.uri("https://oauth2.googleapis.com/token")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
		.body(queryBody)
		.retrieve()
		.body(String.class);
		
//		System.out.println("credentials"+credentials);
		
		// 解析 credentials 拿 token
		JsonNode jsonNode = new ObjectMapper().readTree(credentials);
		String accessToken = jsonNode.get("access_token").asText();
		String idToken = jsonNode.get("id_token").asText();
		
		String result = restClient.get()
		.uri("https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token="+accessToken)
		.header("Authorization", "Bearer "+ idToken)
		.retrieve()
		.body(String.class);
		
//		System.out.println("result: " + result);
		
		// 解析 result 拿到帳號
		JsonNode jsonNode2 = new ObjectMapper().readTree(result);
		String userEmail = jsonNode2.get("email").asText();
		
		System.out.println("userEmail: "+ userEmail);
		
		// 檢查資料庫有沒有，沒有的話幫他新增，有的話讓他登入，給他 Session
		
		return "index";
	}
	

}
