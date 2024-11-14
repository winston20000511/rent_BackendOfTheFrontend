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

	public Long getHouseId() {
		return houseId;
	}

	public void setHouseId(Long houseId) {
		this.houseId = houseId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Short getRoom() {
		return room;
	}

	public void setRoom(Short room) {
		this.room = room;
	}

	public Short getBathroom() {
		return bathroom;
	}

	public void setBathroom(Short bathroom) {
		this.bathroom = bathroom;
	}

	public Short getLivingroom() {
		return livingroom;
	}

	public void setLivingroom(Short livingroom) {
		this.livingroom = livingroom;
	}

	public Short getKitchen() {
		return kitchen;
	}

	public void setKitchen(Short kitchen) {
		this.kitchen = kitchen;
	}

	public Byte getHousetype() {
		return housetype;
	}

	public void setHousetype(Byte housetype) {
		this.housetype = housetype;
	}

	public Short getFloor() {
		return floor;
	}

	public void setFloor(Short floor) {
		this.floor = floor;
	}

	public Boolean getAtticAddition() {
		return atticAddition;
	}

	public void setAtticAddition(Boolean atticAddition) {
		this.atticAddition = atticAddition;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
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