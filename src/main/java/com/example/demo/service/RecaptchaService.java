package com.example.demo.service;

import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.demo.helper.RecaptchaApiConfig;

@Service
public class RecaptchaService {
	
	private static Logger logger = Logger.getLogger(RecaptchaService.class.getName());
	
	@Autowired
	private RecaptchaApiConfig recaptchaApiConfig;
	
	public RecaptchaService(RecaptchaApiConfig recaptchaApiConfig) {
		this.recaptchaApiConfig = recaptchaApiConfig;
	}
	
	private String recaptchaVerifyURL = recaptchaApiConfig.getVerifyurl();
	private String secretKey = recaptchaApiConfig.getSecret();
	
//	private String recaptchaVerifyURL = "https://www.google.com/recaptcha/api/siteverify";
//	private String secretKey = "6LfaGaQqAAAAACMQA_ccPpOzkcw3KAW86HsiRKN5";
			
	public boolean verifyRecaptcha(String recaptchaToken) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
	    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.add("secret", secretKey);
		requestBody.add("response", recaptchaToken);
		
	    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

	    try {
	    	
	        @SuppressWarnings("rawtypes")
			ResponseEntity<Map> responseEntity = restTemplate.postForEntity(recaptchaVerifyURL, requestEntity, Map.class);
	        @SuppressWarnings("unchecked")
	        Map<String, Object> responseBody = responseEntity.getBody();

	        if (responseBody != null && Boolean.TRUE.equals(responseBody.get("success"))) {
	            logger.info("reCAPTCHA 驗證成功：" + responseBody);
	            return true;
	        } else {
	            logger.warning("reCAPTCHA 驗證失敗：" + responseBody);
	        }
	    } catch (Exception e) {
	        logger.severe("reCAPTCHA 驗證錯誤：" + e.getMessage());
	        e.printStackTrace();
	    }
		
		return false;
		
	}
}
