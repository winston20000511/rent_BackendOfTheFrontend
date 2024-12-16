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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter
@Setter
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
	
}
