package com.example.demo.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

	@Column(name = "address")
	private String address;

	@Column(name = "lat")
	private BigDecimal lat;
	
	@Column(name = "lng")
	private BigDecimal lng;

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

	@Column(name = "clickCount")
	private Integer clickCount;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "house", cascade = CascadeType.ALL)
	private List<BookingBean> bookings;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "house", cascade = CascadeType.ALL)
	@JsonManagedReference
	private HouseBookingTimeSlotBean bookingTimeSlot;

	public HouseBean() {

	}

	
	public HouseBean(Long houseId) {
		this.houseId = houseId;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public BigDecimal getLng() {
		return lng;
	}

	public void setLng(BigDecimal lng) {
		this.lng = lng;
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

	public Integer getClickCount() {
		return clickCount;
	}

	public void setClickCount(Integer clickCount) {
		this.clickCount = clickCount;
	}

	public List<BookingBean> getBookings() {
		return bookings;
	}

	public void setBookings(List<BookingBean> bookings) {
		this.bookings = bookings;
	}

	public HouseBookingTimeSlotBean getBookingTimeSlot() {
		return bookingTimeSlot;
	}

	public void setBookingTimeSlot(HouseBookingTimeSlotBean bookingTimeSlot) {
		this.bookingTimeSlot = bookingTimeSlot;
	}

}