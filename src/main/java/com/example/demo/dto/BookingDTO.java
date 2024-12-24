package com.example.demo.dto;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BookingDTO {
	private Long bookingId;
	private Long houseId;
	private Long userId;
	private LocalDateTime createDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date bookingDate;
	private Time bookingTime;
	private Byte status;
	private String message;
}
