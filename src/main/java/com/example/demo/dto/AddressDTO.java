package com.example.demo.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AddressDTO {
	
	private Long houseid;
	private String address;
	private Double lat;
	private Double lng;
	private Integer price;
	private String adName;
	private LocalDateTime paidDate;


}
