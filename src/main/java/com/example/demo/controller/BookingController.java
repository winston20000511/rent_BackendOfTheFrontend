package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.BookingDTO;
import com.example.demo.dto.BookingSlotDTO;
import com.example.demo.model.BookingBean;
import com.example.demo.model.HouseBookingTimeSlotBean;
import com.example.demo.service.BookingService;

import jakarta.mail.MessagingException;
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
	
	@CrossOrigin(origins = "http://localhost:5173") 
	@GetMapping("/api/booking/list")
	public ResponseEntity<?> editTimeSlot(Long houseId) {
		BookingSlotDTO house = bookingService.findTimeSlotByHouseId(houseId);
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
	@GetMapping("/api/booking/guest")
    public ResponseEntity<?> getBookingByUser(Long userId) {
		List<BookingDTO> bookingList = bookingService.getBookingByUser(userId);

        return ResponseEntity.ok().body(bookingList);
    }
	
	@ResponseBody
	@GetMapping("/api/booking/host")
    public ResponseEntity<?> getBookingByHouse(Long houseId) {
		List<BookingDTO> bookingList = bookingService.getBookingByHouse(houseId);
		
        return ResponseEntity.ok().body(bookingList);
    }
	
	@ResponseBody
	@PostMapping("/api/booking/host")
    public ResponseEntity<?> postBooking(@RequestBody BookingDTO bookingDTO) throws MessagingException {
		bookingDTO.setCreateDate(LocalDateTime.now());
		bookingDTO.setStatus((byte) 0);

        return ResponseEntity.ok().body(bookingService.createBooking(bookingDTO));
    }
	
	@ResponseBody
	@PutMapping("/api/booking/guest")
    public ResponseEntity<?> putBookingByUser(@RequestBody BookingDTO bookingDTO) throws MessagingException {
		

        return ResponseEntity.ok().body(bookingService.updateBookingByGuest(bookingDTO));
    }
	
	@ResponseBody
	@PutMapping("/api/booking/host")
    public ResponseEntity<?> putBookingByHouse(@RequestBody BookingDTO bookingDTO) throws MessagingException {


        return ResponseEntity.ok().body(bookingService.updateBookingByHost(bookingDTO));
    }
	
	@ResponseBody
	@GetMapping("test")
	public Optional<BookingBean> getMethodName() {
	
		return null;
	}
	
}
