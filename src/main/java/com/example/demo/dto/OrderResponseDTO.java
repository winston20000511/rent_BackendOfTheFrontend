package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDTO {

	// table 用
	private String merchantTradNo;
	private List<String> houseTitles;
	private LocalDateTime merchantTradDate;
	private String orderStatus;
	
	// 詳細內容用
	private List<Integer> adIds;
 	private List<String> adtypes;
	private Long totalAmount;
	
	public OrderResponseDTO() {
	}
	

	public OrderResponseDTO(String merchantTradNo, List<String> houseTitles, LocalDateTime merchantTradDate,
			String orderStatus, List<String> adtypes, Long totalAmount) {
		super();
		this.merchantTradNo = merchantTradNo;
		this.houseTitles = houseTitles;
		this.merchantTradDate = merchantTradDate;
		this.orderStatus = orderStatus;
		this.adtypes = adtypes;
		this.totalAmount = totalAmount;
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

	public List<String> getAdtypes() {
		return adtypes;
	}

	public void setAdtypes(List<String> adtypes) {
		this.adtypes = adtypes;
	}

	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}

	
	
}
