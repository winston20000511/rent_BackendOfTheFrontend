package com.example.demo.controller;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Optional;

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

import com.example.demo.dto.BookingDTO;
import com.example.demo.dto.BookingDetailDTO;
import com.example.demo.dto.BookingSlotDTO;
import com.example.demo.model.BookingBean;
import com.example.demo.model.HouseBookingTimeSlotBean;
import com.example.demo.repository.BookingRepository;
import com.example.demo.service.BookingService;

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
    public ResponseEntity<?> bookHouse(@RequestBody BookingDTO bookingDTO) {
		
		
		bookingDTO.setCreateDate(LocalDateTime.now());
		bookingDTO.setStatus((byte) 0);
		
        // 發送郵件通知房東
//        String subject = "新預約通知";
//        String body = "房屋名稱: " + houseName + "\n有新的預約請求。";
//        
//        bookingService.sendEmail(landlordEmail, subject, body);
        
		System.out.println(bookingDTO);
		
        return ResponseEntity.ok().body(bookingService.createBooking(bookingDTO));
    }

	@ResponseBody
	@GetMapping("test")
	public Optional<BookingBean> getMethodName() {
	
		return null;
	}
	
}
