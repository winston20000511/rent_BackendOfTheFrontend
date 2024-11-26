package com.example.demo.controller;

import java.net.http.HttpClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.demo.model.HouseBookingTimeSlotBean;
import com.example.demo.service.BookingService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BookingController {
	
	@Autowired
	private BookingService bookingService;
	
	@GetMapping("/booking/house/{id}")
	public String bookingPage(@PathVariable("id") Integer houseId) {

		
		return "bookingPage";
	}

	@GetMapping("/booking/timeSlot/{id}")
	public String editTimeSlot(@PathVariable("id") Integer houseId) {
		
		
		return "bookingTimeSlotView";
	}
	

	@PostMapping("/booking/updataTimeSlot")
	public ResponseEntity<?> updataTimeSlot(@RequestBody HouseBookingTimeSlotBean bean, HttpSession session) {
		Long userId = (Long)session.getAttribute("loginUserId");
		if(userId==null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("/users/login");
		}
		
		bookingService.updataTimeSlot(bean);
		
		return ResponseEntity.ok("/"); 
	}
	
	
}
