package com.example.demo.dto;

import lombok.Data;

@Data
public class HouseOwnerInfoDTO {
    private String name;
    private byte[] picture;
    private String phone;

    // 构造函数
    public HouseOwnerInfoDTO(String name, byte[] picture, String phone) {
        this.name = name;
        this.picture = picture;
        this.phone = phone;
    }

	public HouseOwnerInfoDTO() {
		// TODO Auto-generated constructor stub
	}
}
