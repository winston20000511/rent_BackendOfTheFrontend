package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "houseBookingTimeSlot_table")
public class HouseBookingTimeSlotBean {

	@Id
	private Long houseId;
	@Column(name = "from_date")
	private LocalDate fromDate;
	@Column(name = "to_date")
	private LocalDate toDate;
	@Column(name = "from_time")
	private LocalTime fromTime;
	@Column(name = "to_time")
	private LocalTime toTime;
	@Column(name = "duration")
	private Short duration;
	@Column(name = "week_Day")
	private String weekDay;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JoinColumn(name = "house_id", insertable = false, updatable = false)
	@JsonBackReference
	private HouseTableBean house;

}
