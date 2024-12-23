package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.ToString;

@ToString
public class OrderResponseDTO {

	// table 用
	private String merchantTradNo;
	private List<String> houseTitles;
	private String merchantTradDate;
	private String orderStatus;
	
	// 詳細內容用
	private List<Long> adIds;
	private List<Integer> coupons; // 有coupon的ad
 	private List<String> adtypes;
 	private List<Integer> adtypesPrices;
 	private List<Integer> prices;
	private Long totalAmount;
	private String choosePayment;
	
	public OrderResponseDTO() {
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
		
		if(choosePayment.equals("linepay")) {
			this.choosePayment = "LINEPAY";
		}
	}


	public List<Integer> getCoupons() {
		return coupons;
	}


	public void setCoupons(List<Integer> coupons) {
		this.coupons = coupons;
	}

	public List<Integer> getAdtypesPrices() {
		return adtypesPrices;
	}

	public void setAdtypesPrices(List<Integer> adtypesPrices) {
		this.adtypesPrices = adtypesPrices;
	}
	
}
