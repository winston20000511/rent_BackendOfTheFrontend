package com.example.demo.dto;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BookingDetailDTO {
    private String OwnerName; 
    private String OwnerEmail;
    private String userName; 
    private String userEmail;
    private LocalDateTime createDate;
	private Date bookingDate;
	private Time fromTime;
	private Time toTime;
	private String status; 
}
