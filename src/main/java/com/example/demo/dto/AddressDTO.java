package com.example.demo.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


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
	private int minPrice;
	private int maxPrice;
	private boolean pet;
	private boolean parkingSpace;
	private boolean elevator;
	private boolean balcony;
	private boolean shortTerm;
	private boolean cooking;
	private boolean waterDispenser;
	private boolean fee;
	private int gender;
	private String houseType;
	private String priority;

	public AddressDTO(Long houseid, String address, Double lat, Double lng, Integer price,
					  String adName, LocalDateTime paidDate, boolean pet, boolean parkingSpace,
					  boolean elevator, boolean balcony, boolean shortTerm, boolean cooking,
					  boolean waterDispenser, boolean fee, int gender, String houseType) {
		this.houseid = houseid;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
		this.price = price;
		this.adName = adName;
		this.paidDate = paidDate;
		this.pet = pet;
		this.parkingSpace = parkingSpace;
		this.elevator = elevator;
		this.balcony = balcony;
		this.shortTerm = shortTerm;
		this.cooking = cooking;
		this.waterDispenser = waterDispenser;
		this.fee = fee;
		this.gender = gender;
		this.houseType = houseType;
	}

	public AddressDTO(Long houseid, String address, Double lat, Double lng,
					  Integer price, String adName, LocalDateTime paidDate) {
		this.houseid = houseid;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
		this.price = price;
		this.adName = adName;
		this.paidDate = paidDate;
	}


	public boolean equals(Object o) {
		// 1) 先判斷是否相同參考
		if (this == o) return true;
		// 2) 判斷是否為空、型別是否相同
		if (o == null || getClass() != o.getClass()) return false;
		// 3) cast 成同型別
		AddressDTO that = (AddressDTO) o;

		//使用者 / 對象
		// 4) 依序比對每個欄位
		if (minPrice > that.minPrice) return false;
		if (maxPrice < that.maxPrice) return false;
		if (!pet && that.pet) return false;
		if (!parkingSpace && that.parkingSpace) return false;
		if (!elevator && that.elevator) return false;
		if (!balcony && that.balcony) return false;
		if (!shortTerm && that.shortTerm) return false;
		if (!cooking && that.cooking) return false;
		if (!fee && that.fee) return false;
		if (gender != that.gender && gender != 2) {
			return false;
		}
		// String 物件或其他物件類型要用 equals() 來比對
//        if (origin != null ? !origin.equals(that.origin) : that.origin != null) return false;

		if (houseType.indexOf(that.houseType) == -1) return false;

//        if (priority != null ? !priority.equals(that.priority) : that.priority != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = address != null ? address.hashCode() : 0;
		result = 31 * result + minPrice;
		result = 31 * result + maxPrice;
		result = 31 * result + (pet ? 1 : 0);
		result = 31 * result + (parkingSpace ? 1 : 0);
		result = 31 * result + (elevator ? 1 : 0);
		result = 31 * result + (balcony ? 1 : 0);
		result = 31 * result + (shortTerm ? 1 : 0);
		result = 31 * result + (cooking ? 1 : 0);
		result = 31 * result + (waterDispenser ? 1 : 0);
		result = 31 * result + (fee ? 1 : 0);
		result = 31 * result + gender;
		result = 31 * result + (houseType != null ? houseType.hashCode() : 0);
		result = 31 * result + (priority != null ? priority.hashCode() : 0);
		return result;
	}

}
