package com.example.demo.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookingOwnerListDTO {
	private Long bookingId;
	private Long houseId;
	private String houseTitle;
	private String houseAddress;
	private Long userId;
	private String userName;
	private String userEmail;
	private String userPhone;
	private Byte userStatus;
	private LocalDateTime createDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime bookingDate;
	private Byte status;
}
