package com.example.demo.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "house_table")
public class HouseTableBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "house_id")
	private Long houseId;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "title")
	private String title;

	@Column(name = "price")
	private Long price;

	@Column(name = "description")
	private String description;

	@Column(name = "size")
	private Integer size;

	@Column(name = "address_id")
	private Long addressId;

	@Column(name = "room")
	private Short room;

	@Column(name = "bathroom")
	private Short bathroom;

	@Column(name = "livingroom")
	private Short livingroom;

	@Column(name = "kitchen")
	private Short kitchen;

	@Column(name = "floor")
	private Short floor;

	@Column(name = "atticAddition")
	private Boolean atticAddition;

	@Column(name = "status")
	private Short status;

    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "addressId", insertable = false, updatable = false)
    private AddressTableBean address;

	public AddressTableBean getAddress() {
		return address;
	}

	public void setAddress(AddressTableBean address) {
		this.address = address;
	}



	public long getHouseId() {
		return houseId;
	}

	public void setHouseId(long houseId) {
		this.houseId = houseId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getAddressId() {
		return addressId;
	}

	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}

	public short getRoom() {
		return room;
	}

	public void setRoom(short room) {
		this.room = room;
	}

	public short getBathroom() {
		return bathroom;
	}

	public void setBathroom(short bathroom) {
		this.bathroom = bathroom;
	}

	public short getLivingroom() {
		return livingroom;
	}

	public void setLivingroom(short livingroom) {
		this.livingroom = livingroom;
	}

	public short getKitchen() {
		return kitchen;
	}

	public void setKitchen(short kitchen) {
		this.kitchen = kitchen;
	}


	public short getFloor() {
		return floor;
	}

	public void setFloor(short floor) {
		this.floor = floor;
	}

	public boolean isAtticAddition() {
		return atticAddition;
	}

	public void setAtticAddition(boolean atticAddition) {
		this.atticAddition = atticAddition;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

}