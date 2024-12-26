package com.example.demo.helper;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "google.recaptcha.key")
public class RecaptchaApiConfig {

	private String site;
	
	private String secret;

	private String verifyurl;

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getVerifyurl() {
		return verifyurl;
	}

	public void setVerifyurl(String verifyurl) {
		this.verifyurl = verifyurl;
	}

	

}
