package com.example.demo.model;

import java.time.LocalDateTime;

public class AdBean {
	
	private Long userId;
	
	private Long houseId;
	
	private Integer adtypeId;
	
	private Integer adPrice;
	
	private Boolean isPaid;
	
	private String orderId;
	
	private LocalDateTime paidDate;

	public AdBean() {
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getHouseId() {
		return houseId;
	}

	public void setHouseId(Long houseId) {
		this.houseId = houseId;
	}

	public Integer getAdtypeId() {
		return adtypeId;
	}

	public void setAdtypeId(Integer adtypeId) {
		this.adtypeId = adtypeId;
	}

	public Integer getAdPrice() {
		return adPrice;
	}

	public void setAdPrice(Integer adPrice) {
		this.adPrice = adPrice;
	}

	public Boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public LocalDateTime getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(LocalDateTime paidDate) {
		this.paidDate = paidDate;
	}

}
