package com.example.demo.dto;

import lombok.Data;

@Data
public class AdCreationRequestDTO {

	private Long houseId;
	
	private Integer adtypeId;

	public AdCreationRequestDTO() {

	}
	
	public AdCreationRequestDTO(Long houseId, Integer adtypeId) {
		this.houseId = houseId;
		this.adtypeId = adtypeId;
	}

}
