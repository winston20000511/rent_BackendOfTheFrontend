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
}
