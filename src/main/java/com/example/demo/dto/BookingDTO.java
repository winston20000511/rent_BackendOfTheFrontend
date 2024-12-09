package com.example.demo.dto;

import java.sql.Date;
import java.sql.Time;

import com.example.demo.model.HouseTableBean;
import com.example.demo.model.UserTableBean;

import lombok.Data;

@Data
public class BookingDTO {
	
    private Long houseId; 
    private HouseTableBean house;
    private Long userId;
    private UserTableBean rentUser;
	private Date bookingDate;
	private Time fromTime;
	private Time toTime;
	private String status; 
}
