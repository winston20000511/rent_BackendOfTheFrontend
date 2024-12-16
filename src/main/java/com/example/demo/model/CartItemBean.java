package com.example.demo.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="cart_items")
@Getter
@Setter
public class CartItemBean {

	@Id
	@Column(name="ad_id")
	private Long adId;
	
	@Column(name="cart_id")
	private Integer cartId;
	
	@Column(name="adtype_id")
	private Integer adtypeId;
	
	@Column(name="ad_price")
	private Integer adPrice;
	
	@Column(name="added_date")
	private LocalDateTime addedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id", insertable = false, updatable = false)
	@JsonIgnore
	private CartBean cart;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "cartItem")
	@JsonIgnore
	private AdBean ad;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "adtype_id", insertable = false, updatable = false)
	@JsonIgnore
	private AdtypeBean adtype;
	
	@PrePersist
	public void prePersist() {
		if(this.addedDate == null) {
			this.addedDate = LocalDateTime.now();
		}
	}
}
