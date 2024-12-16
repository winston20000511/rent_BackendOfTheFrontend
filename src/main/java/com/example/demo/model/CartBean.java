package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="cart_table")
@Getter
@Setter
public class CartBean {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="cart_id")
	private Integer cartId;
		
	@Column(name="user_id")
	private Long userId;
	
	@Column(name="created_at")
	private LocalDateTime createdAt;
	
	@OneToMany(mappedBy = "cart", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<CartItemBean> cartItems = new ArrayList<>();
	
	@PrePersist
	public void prePersist() {
		if(this.createdAt == null) {
			this.createdAt = LocalDateTime.now();
		}
	}
}
