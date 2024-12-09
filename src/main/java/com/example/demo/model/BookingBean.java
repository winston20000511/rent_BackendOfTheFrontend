package com.example.demo.model;

import java.sql.Date;
import java.sql.Time;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "booking_table")
@IdClass(BookingId.class)
public class BookingBean {
	
	@Id
    @Column(name = "house_id")
    private Long houseId; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id", insertable = false, updatable = false)
    @JsonIgnore
    private HouseTableBean house;

    @Id
    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private UserTableBean rentUser;

	@Column(name = "booking_date")
	private Date bookingDate;
	
	@Column(name = "start_time")
	private Time fromTime;
	
	@Column(name = "end_time")
	private Time toTime;
	
	@Column(name = "status")
	private String status;

	public boolean isOwner() {
		
		return rentUser.getUserId().equals(house.getUser().getUserId());
	}

}
