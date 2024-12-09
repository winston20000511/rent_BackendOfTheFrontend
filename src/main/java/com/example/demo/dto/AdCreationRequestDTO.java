package com.example.demo.dto;

public class AdCreationRequestDTO {

	private Long userId;
	
	private Long houseId;
	
	private String adName;
	
	private Integer adPrice;
	
	public AdCreationRequestDTO() {
		
	}

	public AdCreationRequestDTO(Long userId, Long houseId, String adName, Integer adPrice) {
		this.userId = userId;
		this.houseId = houseId;
		this.adName = adName;
		this.adPrice = adPrice;
	}

	public Long getUserId() {
		return userId;
	}


	public Long getHouseId() {
		return houseId;
	}

	public String getAdName() {
		return adName;
	}

	public Integer getAdPrice() {
		return adPrice;
	}

}
