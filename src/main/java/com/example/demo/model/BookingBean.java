package com.example.demo.model;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "booking_id")
	private Long bookingId;

	@Column(name = "house_id")
	private Long houseId;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "create_date")
	private LocalDateTime createDate;
	
	@Column(name = "booking_date")
	private Date bookingDate;

	@Column(name = "booking_time")
	private Time bookingTime;
	
	@Column(name = "status")
	private Byte status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "house_id", insertable = false, updatable = false)
	@JsonIgnore
	private HouseTableBean house;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	@JsonIgnore
	private UserTableBean rentUser;

}
