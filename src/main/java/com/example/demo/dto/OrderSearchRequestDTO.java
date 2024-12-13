package com.example.demo.dto;

import java.time.LocalDateTime;

public class OrderSearchRequestDTO {

	// 搜尋條件
	private Long userId;
	private Short orderStatus; // all, active, cancelled
	private String merchantTradNo;
	private String startDate;
	private String endDate;
	private String houseTitle;
	
	public OrderSearchRequestDTO() {
	}

	public OrderSearchRequestDTO(Long userId, Short orderStatus, String merchantTradNo,
			String startDate, String endDate, String houseTitle) {
		this.userId = userId;
		this.orderStatus = orderStatus;
		this.merchantTradNo = merchantTradNo;
		this.startDate = startDate;
		this.endDate = endDate;
		this.houseTitle = houseTitle;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Short getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		if(orderStatus.equals("active")) {
			this.orderStatus = (short) 1;
		}
		
		if(orderStatus.equals("cancelled")) {
			this.orderStatus = (short) 0;
		}
	}

	public String getMerchantTradNo() {
		return merchantTradNo;
	}

	public void setMerchantTradNo(String merchantTradNo) {
		this.merchantTradNo = merchantTradNo;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getHouseTitle() {
		return houseTitle;
	}

	public void setHouseTitle(String houseTitle) {
		this.houseTitle = houseTitle;
	}

	@Override
	public String toString() {
		return "OrderSearchRequestDTO [orderStatus=" + orderStatus + ", merchantTradNo=" + merchantTradNo + ", startDate="
				+ startDate + ", endDate=" + endDate + ", houseTitle=" + houseTitle + "]";
	}
	
}
