package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "booking_table")
public class BookingTableBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "house_id", nullable = false)
    private HouseTableBean house;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserTableBean user;
    @Column(name = "create_date")
    private LocalDate createDate;
    @Column(name = "booking_date")
    private LocalDate bookingDate;
    @Column(name = "start_time")
    private LocalTime startTime;
    @Column(name = "ending_time")
    private LocalTime endTime;
    @Column(name = "status")
    private String status;


	public BookingTableBean() {
		// TODO Auto-generated constructor stub
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public HouseTableBean getHouse() {
		return house;
	}


	public void setHouse(HouseTableBean house) {
		this.house = house;
	}


	public UserTableBean getUser() {
		return user;
	}


	public void setUser(UserTableBean user) {
		this.user = user;
	}


	public LocalDate getCreateDate() {
		return createDate;
	}


	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}


	public LocalDate getBookingDate() {
		return bookingDate;
	}


	public void setBookingDate(LocalDate bookingDate) {
		this.bookingDate = bookingDate;
	}


	public LocalTime getStartTime() {
		return startTime;
	}


	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}


	public LocalTime getEndTime() {
		return endTime;
	}


	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

}
