package com.example.demo.dto;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BookingSlotDTO {
	private Long houseId;
	private Date fromDate;
	private Date toDate;
	private Time fromTime;
	private Time toTime;
	private Short duration;
	private String weekDay;
	private List<String> excludedTime;
}
