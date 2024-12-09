package com.example.demo.dto;

public class AddressDTO {

	private String address;
	private Double lat;
	private Double lng;
	private Integer price;
	private boolean isPaid;
	

	public AddressDTO() {

	}


	public AddressDTO(String address, Double lat, Double lng, Integer price, boolean isPaid) {
		super();
		this.address = address;
		this.lat = lat;
		this.lng = lng;
		this.price = price;
		this.isPaid = isPaid;
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

	public boolean isPaid() {
		return isPaid;
	}

	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}
	

	
}
