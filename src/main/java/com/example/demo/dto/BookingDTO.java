package com.example.demo.dto;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BookingDTO {
	private Long bookingId;
	private Long houseId;
	private Long userId;
	private LocalDateTime createDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate bookingDate;
	private LocalTime bookingTime;
	private Byte status;
	private String message;
}
