package com.example.demo.dto;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BookingDTO {
	private Long bookingId;
	private Long houseId;
	private Long userId;
	private LocalDateTime createDate;
	private Date bookingDate;
	private Time fromTime;
	private Time toTime;
	private Byte status;
}
