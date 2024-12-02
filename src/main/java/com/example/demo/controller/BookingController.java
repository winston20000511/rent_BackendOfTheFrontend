package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.BookingSlotDTO;
import com.example.demo.model.HouseBookingTimeSlotBean;
import com.example.demo.service.BookingService;
import com.example.demo.service.HouseService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BookingController {
	
	
	@Autowired
	private BookingService bookingService;
	
	
	
	@GetMapping("/booking/house/{id}")
	public String bookingPage(@PathVariable("id") Long houseId) {
		return "bookingPage";
	}
	
	@GetMapping("/booking/timeSlot/{id}")
	public String toTimeSlot(@PathVariable("id") Long houseId, HttpSession session) {
		Long userId = (Long)session.getAttribute("loginUserId");
		if(userId==null) {
			return "loginView";
		}
		
		boolean confirm = bookingService.confirm(houseId, userId);
		if(confirm) {
			return "bookingTimeSlotView";
		}
		
		return "redirect:/";
	}
	
	@GetMapping("/api/booking/add")
	public ResponseEntity<?> editTimeSlot(Long houseId) {
		BookingSlotDTO house = bookingService.findTimeSlotByHouseId(1l);
		return ResponseEntity.ok(house);
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
	
	@ResponseBody
	@GetMapping("/booking/editTimeSlot")
	public ResponseEntity<?> editTimeSlot(@RequestParam Long houseId, HttpSession session) {
		Long userId = (Long)session.getAttribute("loginUserId");
		if(userId==null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		BookingSlotDTO bookingTimeSlot = bookingService.findTimeSlotByHouseId(houseId);
		return ResponseEntity.ok().body(bookingTimeSlot);
	}
	
	@ResponseBody
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
