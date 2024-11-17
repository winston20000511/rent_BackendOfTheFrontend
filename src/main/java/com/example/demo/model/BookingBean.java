package com.example.demo.model;

import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "booking_table")
@IdClass(BookingId.class)
public class BookingBean {
	
	@Id
    @Column(name = "house_id")
    private Long houseId; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id", insertable = false, updatable = false)
    private HouseBean house;

    @Id
    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
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

	public HouseBean getHouse() {
		return house;
	}

	public void setHouse(HouseBean house) {
		this.house = house;
	}

	public UserBean getRentUser() {
		return rentUser;
	}

	public void setRentUser(UserBean rentUser) {
		this.rentUser = rentUser;
	}

	public Date getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	public Time getFromTime() {
		return fromTime;
	}

	public void setFromTime(Time fromTime) {
		this.fromTime = fromTime;
	}

	public Time getToTime() {
		return toTime;
	}

	public void setToTime(Time toTime) {
		this.toTime = toTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
