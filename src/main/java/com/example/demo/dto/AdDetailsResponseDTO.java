package com.example.demo.dto;

import java.time.LocalDateTime;

public class AdDetailsResponseDTO {
	
	private Long adId;
	private Long userId;
	private String userName;
	private String houseTitle;
	private String adName;
	private Integer adPrice;
	private String isPaid;
	private String orderId;
	private String paidDate;
	private Long remainingDays;
	
	public AdDetailsResponseDTO() {
	}
	

	public AdDetailsResponseDTO(Long adId, Long userId, String userName, String houseTitle, String adName, Integer adPrice,
			String isPaid, String orderId, String paidDate) {
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

	public String getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		if(isPaid) {
			this.isPaid="已付款";
		}else {
			this.isPaid="未付款";
		}
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(LocalDateTime paidDate) {
		if(paidDate != null) {
			this.paidDate = paidDate.toString().substring(0,10);;			
		}else {
			this.paidDate = "尚未發布";
		}
	}
	
    public Long getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(Long remainingDays) {
        this.remainingDays = remainingDays;
    }


	@Override
	public String toString() {
		return "AdDetailsResponseDTO [adId=" + adId + ", userId=" + userId + ", userName=" + userName + ", houseTitle="
				+ houseTitle + ", adName=" + adName + ", adPrice=" + adPrice + ", isPaid=" + isPaid + ", orderId="
				+ orderId + ", paidDate=" + paidDate + ", remainingDays=" + remainingDays + "]";
	}
	
	
	
}
