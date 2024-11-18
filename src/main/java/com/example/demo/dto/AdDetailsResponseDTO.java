package com.example.demo.dto;

public class AdDetailsResponseDTO {
	
	private String userId;
	private String userName;
	private String houseTitle;
	private String adName;
	private Integer adPrice;
	private String isPaid; // 已付款 & 未付款
	private String orderId;
	private String paidDate;
	
	public AdDetailsResponseDTO() {
	}

	public AdDetailsResponseDTO(String userId, String userName, String houseTitle, String adName, Integer adPrice,
			String isPaid, String orderId, String paidDate) {
		this.userId = userId;
		this.userName = userName;
		this.houseTitle = houseTitle;
		this.adName = adName;
		this.adPrice = adPrice;
		this.isPaid = isPaid;
		this.orderId = orderId;
		this.paidDate = paidDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
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

	public void setIsPaid(String isPaid) {
		this.isPaid = isPaid;
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

	public void setPaidDate(String paidDate) {
		this.paidDate = paidDate;
	}
	
}
