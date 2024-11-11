package com.example.demo.model;

import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "booking_table")
public class Booking {
	
	@EmbeddedId
	private BookingId id;
	private Date date;
	private Time fromTime;
	private Time toTime;
	private String status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	private User user;	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("houseId")
	private Houses houses;

	public Booking() {
		
	}
	
	public boolean isOwner() {
		
		return user.getUserId().equals(houses.getUser().getUserId());
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Houses getHouses() {
		return houses;
	}

	public void setHouses(Houses houses) {
		this.houses = houses;
	}
	
}
