package com.example.demo.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter
@Setter
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
	
	@ManyToOne(fetch = FetchType.EAGER)
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="order_id", insertable=false, updatable=false)
	@JsonIgnore
	private OrderBean order;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="ad_id", referencedColumnName = "id")
	@JsonIgnore
	private CartItemBean cartItem;

}
