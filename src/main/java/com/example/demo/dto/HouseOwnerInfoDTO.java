package com.example.demo.dto;

import lombok.Data;

@Data
public class HouseOwnerInfoDTO {
	private Long userId;
    private String name;
    private byte[] picture; // 原始字节数组
    private String phone;

    public HouseOwnerInfoDTO(Long userId,String name, byte[] picture, String phone) {
        this.userId =userId;
    	this.name = name;
        this.picture = picture;
        this.phone = phone;
    }

    public HouseOwnerInfoDTO() {
   
    }
}