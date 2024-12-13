package com.example.demo.dto;

import java.sql.Date;
import java.sql.Time;

import lombok.Data;
@Data
public class BookingSlotDTO {
	private Long houseId;
	private Date fromDate;
	private Date toDate;
	private Time fromTime;
	private Time toTime;
	private Short duration;
	private String weekDay;
	public Long getHouseId() {
		return houseId;
	}
	public void setHouseId(Long houseId) {
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
	public Time getToTime() {
		return toTime;
	}
	public void setToTime(Time toTime) {
		this.toTime = toTime;
	}
	public Short getDuration() {
		return duration;
	}
	public void setDuration(Short duration) {
		this.duration = duration;
	}
	public String getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	
	
}
