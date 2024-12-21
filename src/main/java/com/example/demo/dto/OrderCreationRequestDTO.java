package com.example.demo.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OrderCreationRequestDTO {
	
	private Integer cartId;
    private List<Long> adIds;
    private List<Long> couponApplied;
    private String thirdParty;
    private String choosePayment;
    private Integer totalAmount;
    
}
