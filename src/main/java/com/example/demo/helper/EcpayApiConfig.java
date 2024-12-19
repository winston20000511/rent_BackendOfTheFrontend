package com.example.demo.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("ecpayapi.properties")
public class EcpayApiConfig {
	
	@Value("${merchantid}")
	private String merchantId;
	
	@Value("${hashkey}")
	private String hashKey;
	
	@Value("${hashiv}")
	private String hashIV;

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getHashKey() {
		return hashKey;
	}

	public void setHashKey(String hashKey) {
		this.hashKey = hashKey;
	}

	public String getHashIV() {
		return hashIV;
	}

	public void setHashIV(String hashIV) {
		this.hashIV = hashIV;
	}
	
}

