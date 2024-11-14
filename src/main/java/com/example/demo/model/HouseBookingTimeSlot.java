package com.example.demo.model;

import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
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
	
	@EmbeddedId
	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "house_id")
	private HouseBean HouseId;
	
	@Column(name = "form_date")
	private Date fromDate;
	@Column(name = "to_date")
	private Date toDate;
	@Column(name = "form_time")
	private Time fromTime;
	@Column(name = "to_time")
	private Time to_date;
	@Column(name = "duration")
	private Short duration;
	@Column(name = "week_day")
	private Short weekDay;
	
	public HouseBookingTimeSlot() {
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


	public Short getDuration() {
		return duration;
	}


	public void setDuration(Short duration) {
		this.duration = duration;
	}


	public Short getWeekDay() {
		return weekDay;
	}


	public void setWeekDay(Short weekDay) {
		this.weekDay = weekDay;
	}


	public HouseBean getHouses() {
		return HouseId;
	}


	public void setHouses(HouseBean houses) {
		HouseId = houses;
	}

}
