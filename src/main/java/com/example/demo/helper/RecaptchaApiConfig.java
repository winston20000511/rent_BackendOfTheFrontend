package com.example.demo.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RecaptchaApiConfig {

	@Value("${reCaptchaSiteKey}")
	private String site;
	
	@Value("${reCaptchaSecretKey}")
	private String secret;

	@Value("${verifyurl}")
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
