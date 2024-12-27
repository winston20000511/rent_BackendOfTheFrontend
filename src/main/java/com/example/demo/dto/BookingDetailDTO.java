package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingDetailDTO {
	private Long ownerId;
	private String ownerName;
	private String ownerEmail;
	private String userName;
	private String userEmail;
	private LocalDateTime createDate;
	private LocalDate bookingDate;
	private LocalTime bookingTime;
	private Byte status;
}
