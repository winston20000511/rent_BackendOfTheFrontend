package com.example.demo.dto;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingDetailDTO {
	private String ownerName;
	private String ownerEmail;
	private String userName;
	private String userEmail;
	private LocalDateTime createDate;
	private Date bookingDate;
	private Time bookingTime;
	private Byte status;
}
