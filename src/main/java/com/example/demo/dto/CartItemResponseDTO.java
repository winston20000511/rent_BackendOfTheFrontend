package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CartItemResponseDTO {

	private Long adId;
	private Integer cartId;
	private String adName;
	private Integer adPrice;
	private String houseTitle;
	private String addedDate;
	
	public void setAddedDate(LocalDateTime addedDate) {
		this.addedDate = addedDate.toString().substring(0,10);
	}
}
