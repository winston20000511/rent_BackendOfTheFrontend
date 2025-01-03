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
    private Short thirdParty;
    private String choosePayment;
    private Integer totalAmount;
    
    public void setThirdParty(String thirdParty) {
    	if(thirdParty.equals("ecpay")) this.thirdParty = 0;
    	if(thirdParty.equals("linepay")) this.thirdParty = 1;
    }
}
