package com.example.demo.dto;

public class AdCreationRequestDTO {

	private Long userId;
	
	private Long houseId;
	
	private Integer adtypeId;
	
	private Integer adPrice;
	
	public AdCreationRequestDTO() {
		
	}

	public AdCreationRequestDTO(Long userId, Long houseId, Integer adtypeId, Integer adPrice) {
		this.userId = userId;
		this.houseId = houseId;
		this.adtypeId = adtypeId;
		this.adPrice = adPrice;
	}

	public Long getUserId() {
		return userId;
	}


	public Long getHouseId() {
		return houseId;
	}

	public Integer getAdtypeId() {
		return adtypeId;
	}

	public Integer getAdPrice() {
		return adPrice;
	}

}
