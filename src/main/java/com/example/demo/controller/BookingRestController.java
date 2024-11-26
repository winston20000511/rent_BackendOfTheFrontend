package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.HouseBookingTimeSlotBean;
import com.example.demo.service.BookingService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
public class BookingRestController {
	
	@Autowired
	private BookingService bookingService;
	
	
	
	private BookingRestController(BookingService bookingService) {
		this.bookingService = bookingService;
	}
	
	@GetMapping("/booking/editTimeSlot")
	public ResponseEntity<?> getBookingTimeSlot(@RequestParam Long houseId, HttpSession session) {
		Long userId = (Long)session.getAttribute("loginUserId");
		if(userId==null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		HouseBookingTimeSlotBean bookingTimeSlot = bookingService.findTimeSlotByHouseId(houseId);

		
		return ResponseEntity.ok().body(bookingTimeSlot);
	}
	
	
	
	
	@PostMapping("/api/house/book")
    public String bookHouse(@RequestParam String houseName, @RequestParam String landlordEmail) {
        // 處理預約邏輯，例如保存到資料庫等
        
        // 發送郵件通知房東
        String subject = "新預約通知";
        String body = "房屋名稱: " + houseName + "\n有新的預約請求。";
        
        bookingService.sendEmail(landlordEmail, subject, body);
        
        return "預約成功，已通知房東！";
    }
	
	
}
