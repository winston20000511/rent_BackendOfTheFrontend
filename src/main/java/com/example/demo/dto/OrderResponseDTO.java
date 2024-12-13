package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDTO {

	// table 用
	private String merchantTradNo;
	private List<String> houseTitles;
	private String merchantTradDate;
	private String orderStatus;
	
	// 詳細內容用
	private List<Long> adIds;
 	private List<String> adtypes;
 	private List<Integer> prices;
	private Long totalAmount;
	private String choosePayment;
	
	public OrderResponseDTO() {
	}
	

	public OrderResponseDTO(String merchantTradNo, List<String> houseTitles, String merchantTradDate,
			String orderStatus, List<Long> adIds, List<String> adtypes, Long totalAmount, String choosePayment) {
		this.merchantTradNo = merchantTradNo;
		this.houseTitles = houseTitles;
		this.merchantTradDate = merchantTradDate;
		this.orderStatus = orderStatus;
		this.adIds = adIds;
		this.adtypes = adtypes;
		this.totalAmount = totalAmount;
		this.choosePayment = choosePayment;
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

	public String getMerchantTradDate() {
		return merchantTradDate;
	}

	public void setMerchantTradDate(LocalDateTime merchantTradDate) {
		this.merchantTradDate = merchantTradDate.toString().substring(0,10);
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Short orderStatus) {
		if(orderStatus == (short)0) {
			this.orderStatus = "已取消";
		}
		
		if(orderStatus == (short)1) {
			this.orderStatus = "一般訂單";
		}
		
		if(orderStatus == (short)2) {
			this.orderStatus = "取消中";
		}
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


	public List<Long> getAdIds() {
		return adIds;
	}


	public void setAdIds(List<Long> adIds) {
		this.adIds = adIds;
	}


	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}


	public List<Integer> getPrices() {
		return prices;
	}


	public void setPrices(List<Integer> prices) {
		this.prices = prices;
	}


	public String getChoosePayment() {
		return choosePayment;
	}


	public void setChoosePayment(String choosePayment) {
		if(choosePayment.equals("Credit")) {
			this.choosePayment = "信用卡";
		}
	}
	
}
