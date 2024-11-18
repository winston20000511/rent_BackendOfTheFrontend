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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserTableBean user;
    @Column(name = "title")
    private String title;
    @Column(name = "price")
    private Integer price;
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
    private Byte room;
    @Column(name = "bathroom")
    private Byte bathroom;
    @Column(name = "livingroom")
    private Byte livingroom;
    @Column(name = "kitchen")
    private Byte kitchen;
    @Column(name = "floor")
    private Byte floor;
    @Column(name = "atticAddition")
    private Boolean atticAddition;
    @Column(name = "status")
    private Byte status;
    @Column(name = "clickCount")
    private Integer clickCount;

    @OneToOne(mappedBy = "house", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private ConditionTableBean condition;

    @OneToOne(mappedBy = "house", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private FurnitureTableBean furniture;

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<HouseImageTableBean> images;

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<BookingBean> bookings;

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<HouseBookingTimeSlotBean> bookingTimeSlots;

	public Long getHouseId() {
		return houseId;
	}

	public void setHouseId(Long houseId) {
		this.houseId = houseId;
	}

	public UserTableBean getUser() {
		return user;
	}

	public void setUser(UserTableBean user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
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

	public Byte getRoom() {
		return room;
	}

	public void setRoom(Byte room) {
		this.room = room;
	}

	public Byte getBathroom() {
		return bathroom;
	}

	public void setBathroom(Byte bathroom) {
		this.bathroom = bathroom;
	}

	public Byte getLivingroom() {
		return livingroom;
	}

	public void setLivingroom(Byte livingroom) {
		this.livingroom = livingroom;
	}

	public Byte getKitchen() {
		return kitchen;
	}

	public void setKitchen(Byte kitchen) {
		this.kitchen = kitchen;
	}

	public Byte getFloor() {
		return floor;
	}

	public void setFloor(Byte floor) {
		this.floor = floor;
	}

	public Boolean getAtticAddition() {
		return atticAddition;
	}

	public void setAtticAddition(Boolean atticAddition) {
		this.atticAddition = atticAddition;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Integer getClickCount() {
		return clickCount;
	}

	public void setClickCount(Integer clickCount) {
		this.clickCount = clickCount;
	}

	public ConditionTableBean getCondition() {
		return condition;
	}

	public void setCondition(ConditionTableBean condition) {
		this.condition = condition;
	}

	public FurnitureTableBean getFurniture() {
		return furniture;
	}

	public void setFurniture(FurnitureTableBean furniture) {
		this.furniture = furniture;
	}

	public List<HouseImageTableBean> getImages() {
		return images;
	}

	public void setImages(List<HouseImageTableBean> images) {
		this.images = images;
	}

	public List<BookingBean> getBookings() {
		return bookings;
	}

	public void setBookings(List<BookingBean> bookings) {
		this.bookings = bookings;
	}

	public List<HouseBookingTimeSlotBean> getBookingTimeSlots() {
		return bookingTimeSlots;
	}

	public void setBookingTimeSlots(List<HouseBookingTimeSlotBean> bookingTimeSlots) {
		this.bookingTimeSlots = bookingTimeSlots;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
