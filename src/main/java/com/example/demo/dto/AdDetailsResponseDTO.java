package com.example.demo.dto;

import java.time.LocalDateTime;

public class AdDetailsResponseDTO {
	
	private Long adId;
	private Long userId;
	private String userName;
	private String houseTitle;
	private String adName;
	private Integer adPrice;
	private Boolean isPaid;
	private String orderId;
	private LocalDateTime paidDate;
	
	public AdDetailsResponseDTO() {
	}
	

	public AdDetailsResponseDTO(Long adId, Long userId, String userName, String houseTitle, String adName, Integer adPrice,
			Boolean isPaid, String orderId, LocalDateTime paidDate) {
		this.adId = adId;
		this.userId = userId;
		this.userName = userName;
		this.houseTitle = houseTitle;
		this.adName = adName;
		this.adPrice = adPrice;
		this.isPaid = isPaid;
		this.orderId = orderId;
		this.paidDate = paidDate;
	}

	public Long getAdId() {
		return adId;
	}

	public void setAdId(Long adId) {
		this.adId = adId;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHouseTitle() {
		return houseTitle;
	}

	public void setHouseTitle(String houseTitle) {
		this.houseTitle = houseTitle;
	}

	public String getAdName() {
		return adName;
	}

	public void setAdName(String adName) {
		this.adName = adName;
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


	@Override
	public String toString() {
		return "AdDetailsResponseDTO [adId=" + adId + ", userId=" + userId + ", userName=" + userName + ", houseTitle="
				+ houseTitle + ", adName=" + adName + ", adPrice=" + adPrice + ", isPaid=" + isPaid + ", orderId="
				+ orderId + ", paidDate=" + paidDate + "]";
	}
	
	
	
}
