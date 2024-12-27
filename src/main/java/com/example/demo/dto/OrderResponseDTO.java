package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class OrderResponseDTO {

	// table 用
	private String merchantTradNo;
	private List<String> houseTitles;
	private List<Long> houseIds;
	private String merchantTradDate;
	private String orderStatus;
	
	// 詳細內容用
	private List<Long> adIds;
	private List<Integer> coupons; // 有coupon的ad
 	private List<String> adtypes;
 	private List<Integer> adtypesPrices;
 	private List<Integer> prices;
	private Long totalAmount;
	private String choosePayment;


	public void setMerchantTradDate(LocalDateTime merchantTradDate) {
		this.merchantTradDate = merchantTradDate.toString().substring(0,10);
	}


	public void setOrderStatus(Short orderStatus) {
		if(orderStatus == (short)0) {
			this.orderStatus = "已取消";
		}
		
		if(orderStatus == (short)1) {
			this.orderStatus = "一般訂單";
		}
		
		if(orderStatus == (short)2) {
			this.orderStatus = "取消中";
		}
	}


	public void setChoosePayment(String choosePayment) {
		if(choosePayment.equals("Credit")) {
			this.choosePayment = "信用卡";
		}
		
		if(choosePayment.equals("linepay")) {
			this.choosePayment = "LINEPAY";
		}
	}

	
}
