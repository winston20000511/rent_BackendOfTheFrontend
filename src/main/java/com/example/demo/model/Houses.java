package com.example.demo.model;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "house_table")
public class Houses {
	@Id
	@Column(name = "house_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer houseId;
	
	@Column(name = "user_id" , insertable = false, updatable = false)
	private Integer	userId;
	private String	title;
	private Integer	price;
	private String	description;
	private Integer	size;
	private String	city;
	private String	township;
	private String	street;
	private Byte	room;
	private Byte	bathroom;
	private Byte	livingroom;
	private Byte	kitchen;
	private Byte	housetype;
	private Byte	floor;
	private Boolean	atticAddition;
	private Byte status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "Houses", cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private HouseBookingTimeSlot bookingTimeSlot;

	public Houses() {
		
	}

	public Integer getHouseId() {
		return houseId;
	}

	public void setHouseId(Integer houseId) {
		this.houseId = houseId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTownship() {
		return township;
	}

	public void setTownship(String township) {
		this.township = township;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
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

	public Byte getHousetype() {
		return housetype;
	}

	public void setHousetype(Byte housetype) {
		this.housetype = housetype;
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

	public HouseBookingTimeSlot getBookingTimeSlot() {
		return bookingTimeSlot;
	}

	public void setBookingTimeSlot(HouseBookingTimeSlot bookingTimeSlot) {
		this.bookingTimeSlot = bookingTimeSlot;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
