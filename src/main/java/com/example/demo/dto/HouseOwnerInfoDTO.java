package com.example.demo.dto;

import lombok.Data;

@Data
public class HouseOwnerInfoDTO {
    private String name;
    private String phone;
    private String base64Picture;

    public HouseOwnerInfoDTO(String name, String phone, String base64Picture) {
        this.name = name;
        this.phone = phone;
        this.base64Picture = base64Picture;
    }

	public HouseOwnerInfoDTO() {
		// TODO Auto-generated constructor stub
	}
}
