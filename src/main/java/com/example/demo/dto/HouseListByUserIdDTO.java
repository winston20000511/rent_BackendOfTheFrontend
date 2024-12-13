package com.example.demo.dto;

import lombok.Data;

@Data
public class HouseListByUserIdDTO {
private Long houseId;
private Long userId;
public HouseListByUserIdDTO(Long houseId, Long userId) {
    this.houseId = houseId;
    this.userId = userId;
}
}
