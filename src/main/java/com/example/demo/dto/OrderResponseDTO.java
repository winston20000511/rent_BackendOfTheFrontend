package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDTO {

	private String merchantTradNo;
	private List<String> houseTitles;
	private LocalDateTime merchantTradDate;
	private String orderStatus;
	
	public OrderResponseDTO() {
	}

	public OrderResponseDTO(String merchantTradNo, List<String> houseTitles, LocalDateTime merchantTradDate,
			String orderStatus) {
		this.merchantTradNo = merchantTradNo;
		this.houseTitles = houseTitles;
		this.merchantTradDate = merchantTradDate;
		this.orderStatus = orderStatus;
	}

	public String getMerchantTradNo() {
		return merchantTradNo;
	}

	public void setMerchantTradNo(String merchantTradNo) {
		this.merchantTradNo = merchantTradNo;
	}

	public List<String> getHouseTitles() {
		return houseTitles;
	}

	public void setHouseTitles(List<String> houseTitles) {
		this.houseTitles = houseTitles;
	}

	public LocalDateTime getMerchantTradDate() {
		return merchantTradDate;
	}

	public void setMerchantTradDate(LocalDateTime merchantTradDate) {
		this.merchantTradDate = merchantTradDate;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
}
