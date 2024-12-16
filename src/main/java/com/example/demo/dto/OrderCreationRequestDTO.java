package com.example.demo.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderCreationRequestDTO {

	// 使用者確認購物車時使用 => 要去改 orders/create
	private Integer cartId;
	private String paymentMethod; // 1 綠界 & 2 LINEPAY
	
	// 共同屬性
//	private String orderId; //綠界變數名稱 merchantTradNo; 皆不可重複
	private Integer totalAmount;
//	private LocalDateTime orderDate;
	private String itemDescription;
	private String productNames;
//	private String checkValue;
	private String choosePayment; // 綠界付款方式
	
}
