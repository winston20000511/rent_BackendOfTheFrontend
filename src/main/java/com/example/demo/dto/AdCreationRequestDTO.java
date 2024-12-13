package com.example.demo.dto;

public class AdCreationRequestDTO {

	private Long userId;
	
	private Long houseId;
	
	private Integer adtypeId;

	public AdCreationRequestDTO() {

	}
	
	public AdCreationRequestDTO(Long userId, Long houseId, Integer adtypeId) {

		this.userId = userId;
		this.houseId = houseId;
		this.adtypeId = adtypeId;
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

}
