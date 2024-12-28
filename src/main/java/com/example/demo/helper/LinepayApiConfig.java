package com.example.demo.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class LinepayApiConfig {
	
	@Value("${channelid}")
	private String linepayChannelIdKey;
	
	@Value("${channelsecretkey}")
	private String linpayChannelSecretKey;

	public String getLinepayChannelIdKey() {
		return linepayChannelIdKey;
	}

	public void setLinepayChannelIdKey(String linepayChannelIdKey) {
		this.linepayChannelIdKey = linepayChannelIdKey;
	}

	public String getLinpayChannelSecretKey() {
		return linpayChannelSecretKey;
	}

	public void setLinpayChannelSecretKey(String linpayChannelSecretKey) {
		this.linpayChannelSecretKey = linpayChannelSecretKey;
	}
	
}
