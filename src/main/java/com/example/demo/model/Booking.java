package com.example.demo.model;

import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "booking_table")
public class Booking {
	
	private Integer id;
	private Integer userId;
	private Integer houseId;
	private Date date;
	private Time fromTime;
	private Time toTime;
	private String status;
}
