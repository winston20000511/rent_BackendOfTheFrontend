package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class BookingController {
	
	@GetMapping("/booking/house/{id}")
	public String bookingPage(@PathVariable("id") Integer houseId) {

		
		return "bookingPage";
	}

	@GetMapping("/booking/timeSlot/{id}")
	public String editTimeSlot(@PathVariable("id") Integer houseId) {
		
		
		return "bookingTimeSlotView";
	}
	
	
	
	
	
}
