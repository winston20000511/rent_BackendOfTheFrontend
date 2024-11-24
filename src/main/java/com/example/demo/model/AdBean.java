package com.example.demo.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ads_table")
public class AdBean {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name= " ad_id")
	private Long adId;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "house_id")
	private Long houseId;
	
	@Column(name = "adtype_id")
	private Integer adtypeId;
	
	@Column(name = "ad_price")
	private Integer adPrice;
	
	@Column(name = "is_paid")
	private Boolean isPaid;
	
	@Column(name = "order_id")
	private String orderId;
	
	@Column(name = "paid_date")
	private LocalDateTime paidDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="adtype_id", insertable=false, updatable=false)
	@JsonIgnore
	private AdtypeBean adtype;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	@JsonIgnore
	private UserTableBean user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="house_id", insertable=false, updatable=false)
	@JsonIgnore
	private HouseTableBean house;

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

	public Long getAdId() {
		return adId;
	}

	public void setAdId(Long adId) {
		this.adId = adId;
	}

	public AdtypeBean getAdtype() {
		return adtype;
	}

	public void setAdtype(AdtypeBean adtype) {
		this.adtype = adtype;
	}

	public HouseTableBean getHouse() {
		return house;
	}

	public void setHouse(HouseTableBean house) {
		this.house = house;
	}

	
}
