package com.example.demo.model;

import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "booking_table")
public class BookingBean {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("houseId")
	@JoinColumn(name = "house_id")
	private HouseBean houses;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	@JoinColumn(name = "user_id")
	private UserBean rentUser;	
	
	@EmbeddedId
	private BookingId id;
	private Date date;
	private Time fromTime;
	private Time toTime;
	private String status;
	
	public BookingBean() {
		
	}
	
	public boolean isOwner() {
		
		return rentUser.getUserId().equals(houses.getUser().getUserId());
	}

	public BookingId getId() {
		return id;
	}

	public void setId(BookingId id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Time getFromTime() {
		return fromTime;
	}

	public void setFromTime(Time fromTime) {
		this.fromTime = fromTime;
	}

	public Time getToTime() {
		return toTime;
	}

	public void setToTime(Time toTime) {
		this.toTime = toTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UserBean getUser() {
		return rentUser;
	}

	public void setUser(UserBean user) {
		this.rentUser = user;
	}

	public HouseBean getHouses() {
		return houses;
	}

	public void setHouses(HouseBean houses) {
		this.houses = houses;
	}
	
}
