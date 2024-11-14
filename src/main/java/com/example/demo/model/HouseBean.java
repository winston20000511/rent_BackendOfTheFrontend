package com.example.demo.model;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "house_table")
public class HouseBean {
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

	@Column(name = "room")
	private Short room;

	@Column(name = "bathroom")
	private Short bathroom;

	@Column(name = "livingroom")
	private Short livingroom;

	@Column(name = "kitchen")
	private Short kitchen;

	@Column(name = "housetype")
	private Byte housetype;

	@Column(name = "floor")
	private Short floor;

	@Column(name = "atticAddition")
	private Boolean atticAddition;

	@Column(name = "status")
	private Short status;

	@Column(name = "address_id")
	private Long address;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "HouseId", cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private HouseBookingTimeSlot bookingTimeSlot;

	public HouseBean() {
		
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

	public byte getHousetype() {
		return housetype;
	}

	public void setHousetype(byte housetype) {
		this.housetype = housetype;
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

	public Long getAddress() {
		return address;
	}

	public void setAddress(Long address) {
		this.address = address;
	}

	public HouseBookingTimeSlot getBookingTimeSlot() {
		return bookingTimeSlot;
	}

	public void setBookingTimeSlot(HouseBookingTimeSlot bookingTimeSlot) {
		this.bookingTimeSlot = bookingTimeSlot;
	}

}