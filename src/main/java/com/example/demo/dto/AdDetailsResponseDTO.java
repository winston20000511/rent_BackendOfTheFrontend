package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class AdDetailsResponseDTO {
	
	private Long adId;
	private Long userId;
	private Long houseId;
	private String houseTitle;
	private String adName;
	private Integer adPrice;
	private String isPaid;
	private String orderId;
	private String paidDate;
	private Long remainingDays;

	public AdDetailsResponseDTO(Long adId, Long userId, Long houseId, String houseTitle, String adName, Integer adPrice,
			String isPaid, String orderId, String paidDate) {
		this.adId = adId;
		this.userId = userId;
		this.houseId = houseId;
		this.houseTitle = houseTitle;
		this.adName = adName;
		this.adPrice = adPrice;
		this.isPaid = isPaid;
		this.orderId = orderId;
		this.paidDate = paidDate;
	}


	public void setIsPaid(Boolean isPaid) {
		if(isPaid) {
			this.isPaid="已付款";
		}else {
			this.isPaid="未付款";
		}
	}

	public void setPaidDate(LocalDateTime paidDate) {
		if(paidDate != null) {
			this.paidDate = paidDate.toString().substring(0,10);;			
		}else {
			this.paidDate = "尚未發布";
		}
	}
	
	
}
