package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders_table")
public class OrderBean {
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "merchant_id")
	private String merchantId;
	
	@Id
	@Column(name = "merchantTradNo")
	private String merchantTradNo;
	
	@Column(name = "merchantTradDate")
	private LocalDateTime merchantTradDate;
	
	@Column(name = "totalAmount")
	private Long totalAmount;
	
	@Column(name = "tradeDesc")
	private String tradeDesc;
	
	@Column(name = "itemName")
	private String itemName;
	
	@Column(name = "order_status", columnDefinition = "TINYINT")
	private Short orderStatus;
	
	@Column(name = "returnUrl")
	private String returnUrl;
	
	@Column(name = "choosePayment")
	private String choosePayment;
	
	@Column(name = "checkMacValue")
	private String checkMacValue;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id", insertable=false, updatable=false)
	@JsonIgnore
	private UserTableBean user;
	
	@OneToMany(mappedBy="order", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<AdBean> ads;
	
	public OrderBean() {
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantTradNo() {
		return merchantTradNo;
	}

	public void setMerchantTradNo(String merchantTradNo) {
		this.merchantTradNo = merchantTradNo;
	}

	public LocalDateTime getMerchantTradDate() {
		return merchantTradDate;
	}

	public void setMerchantTradDate(LocalDateTime merchantTradDate) {
		this.merchantTradDate = merchantTradDate;
	}

	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getTradeDesc() {
		return tradeDesc;
	}

	public void setTradeDesc(String tradeDesc) {
		this.tradeDesc = tradeDesc;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Short getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Short orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getChoosePayment() {
		return choosePayment;
	}

	public void setChoosePayment(String choosePayment) {
		this.choosePayment = choosePayment;
	}

	public String getCheckMacValue() {
		return checkMacValue;
	}

	public void setCheckMacValue(String checkMacValue) {
		this.checkMacValue = checkMacValue;
	}

	public UserTableBean getUser() {
		return user;
	}

	public void setUser(UserTableBean user) {
		this.user = user;
	}

	public List<AdBean> getAds() {
		return ads;
	}

	public void setAds(List<AdBean> ads) {
		this.ads = ads;
	}

}
