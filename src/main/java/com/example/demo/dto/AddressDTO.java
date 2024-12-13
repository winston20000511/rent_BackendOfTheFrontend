package com.example.demo.dto;
import java.time.LocalDateTime;

public class AddressDTO {

	private String address;
	private Double lat;
	private Double lng;
	private Integer price;
	private String adName;
	private LocalDateTime paidDate;

	public AddressDTO() {
        
	}

	public AddressDTO(String address, Double lat, Double lng, Integer price ,String adName, LocalDateTime paidDate) {
		this.address = address;
		this.lat = lat;
		this.lng = lng;
		this.price = price;
		this.adName = adName;
		this.paidDate = paidDate;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getAdName() {
		return adName;
	}
	public void setAdName(String adName) {
		this.adName = adName;
	}

	public LocalDateTime getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(LocalDateTime paidDate) {
		this.paidDate = paidDate;
	}

}
