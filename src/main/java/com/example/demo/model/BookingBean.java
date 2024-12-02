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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "booking_table")
public class BookingBean {
	
	@Id
    @Column(name = "house_id")
    private Long houseId; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id", insertable = false, updatable = false)
    private HouseTableBean house;

    
    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
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
