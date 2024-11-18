package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.HouseBookingTimeSlotBean;
import com.example.demo.service.BookingService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class BookingRestController {
	
	
	private BookingService bookingService;
	
	private BookingRestController(BookingService bookingService) {
		this.bookingService = bookingService;
	}
	
	@GetMapping("/booking/timeSlot")
	public HouseBookingTimeSlotBean getBookingTimeSlot(@RequestParam Long houseId, HttpSession session) {
		Long userId = (Long)session.getAttribute("loginUserId");
		if(userId==null) {
			return null;
		}
		
		HouseBookingTimeSlotBean bookingTimeSlot = bookingService.findTimeSlotByHouseId(houseId);

		
		return bookingTimeSlot;
	}
	
	
	
	
}
