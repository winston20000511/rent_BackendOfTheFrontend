package com.example.demo.dto;

import lombok.Data;

@Data
public class HouseOwnerInfoDTO {
    private String name;
    private String base64Picture;
    private String phone;

    // 构造函数
    public HouseOwnerInfoDTO(String name, String base64Picture, String phone) {
        this.name = name;
        this.base64Picture = base64Picture;
        this.phone = phone;
    }

	public HouseOwnerInfoDTO() {
		// TODO Auto-generated constructor stub
	}
}
