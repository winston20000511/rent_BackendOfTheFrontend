package com.example.demo.model;

import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "booking_table")
public class BookingBean {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "booking_id")
	private Long id; 
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "house_id")
	private HouseBean house;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserBean rentUser;	

	@Column(name = "booking_date")
	private Date bookingDate;
	
	@Column(name = "start_time")
	private Time fromTime;
	
	@Column(name = "end_time")
	private Time toTime;
	
	@Column(name = "status")
	private String status;
	
	public BookingBean() {
		
	}
	
	public boolean isOwner() {
		
		return rentUser.getUserId().equals(house.getUserId());
	}
	
	
}
