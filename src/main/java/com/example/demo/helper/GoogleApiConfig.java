package com.example.demo.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("googleapikey.properties")
public class GoogleApiConfig {
	
	@Value("${googleApiKey}")
	private String googleMapKey;

	public String getGoogleMapKey() {
		return googleMapKey;
	}

	public void setGoogleMapKey(String googleMapKey) {
		this.googleMapKey = googleMapKey;
	}

}
