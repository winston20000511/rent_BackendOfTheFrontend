package com.example.demo.service;

import java.util.Map;
import java.util.logging.Logger;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.helper.RecaptchaApiConfig;

@Service
public class RecaptchaService {
	
	private static Logger logger = Logger.getLogger(RecaptchaService.class.getName());
	
	private RecaptchaApiConfig recaptchaApiConfig;
	
	public RecaptchaService(RecaptchaApiConfig recaptchaApiConfig) {
		this.recaptchaApiConfig = recaptchaApiConfig;
	}
	
	public boolean verifyRecaptcha(String recaptchaToken) {
		RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

	    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
	    requestBody.add("secret", recaptchaApiConfig.getSecret());
	    requestBody.add("response", recaptchaToken);
	    logger.info("後端的request body: " + requestBody);

	    // MultiValueMap: 存一個鍵對應多個值
	    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

	    try {
	    	
	        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(recaptchaApiConfig.getVerifyurl(), requestEntity, Map.class);
	        Map<String, Object> responseBody = responseEntity.getBody();

	        if (responseBody != null && Boolean.TRUE.equals(responseBody.get("success"))) {
	            logger.info("reCAPTCHA 驗證成功：" + responseBody);
	            return true;
	        } else {
	            logger.warning("reCAPTCHA 驗證失敗：" + responseBody);
	        }
	        
	    } catch(HttpStatusCodeException e) {
	    	logger.info("HTTP狀態異常: " + e.getStatusCode() + ", 回應: " + e.getResponseBodyAsString());
	    } catch(RestClientException e) {
	    	logger.info("RestTemplate 發送請求時出現異常：" + e.getMessage());
	    } catch (ClassCastException e) {
	    	logger.info("回應內容無法轉換為 Map：" + e.getMessage());
	    } catch (Exception e) {
	        logger.severe("reCAPTCHA 驗證錯誤：" + e.getMessage());
	    }
		
		return false;
		
	}
}
