package com.example.demo.model;

import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "houseBookingTimeSlot_tabel")
public class HouseBookingTimeSlot {

	@Id
	@Column(name = "id")
	private Integer houseId;
	@Column(name = "form_date")
	private Date fromDate;
	@Column(name = "to_date")
	private Date toDate;
	@Column(name = "form_time")
	private Time fromTime;
	@Column(name = "to_time")
	private Time to_date;
	@Column(name = "duration")
	private Integer duration;

	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "house_id")
	private Houses Houses;

	public HouseBookingTimeSlot() {
	}

	public Integer getHouseId() {
		return houseId;
	}

	public void setHouseId(Integer houseId) {
		this.houseId = houseId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Time getFromTime() {
		return fromTime;
	}

	public void setFromTime(Time fromTime) {
		this.fromTime = fromTime;
	}

	public Time getTo_date() {
		return to_date;
	}

	public void setTo_date(Time to_date) {
		this.to_date = to_date;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Houses getHouses() {
		return Houses;
	}

	public void setHouses(Houses houses) {
		Houses = houses;
	}
	
}
