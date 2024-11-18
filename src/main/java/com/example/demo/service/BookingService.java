package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.BookingRepository;
import com.example.demo.model.BookingTimeSlotRepository;
import com.example.demo.model.HouseBookingTimeSlotBean;

@Service
public class BookingService {
	
	@Autowired
	private BookingRepository bookingRepo;
	
	@Autowired
	public BookingTimeSlotRepository timeSlotRepo;
	
	
	public HouseBookingTimeSlotBean findTimeSlotByHouseId(Long houseId) {
		Optional<HouseBookingTimeSlotBean> op = timeSlotRepo.findById(houseId);
		
		if(op.isPresent()) {
			return op.get();
		}else {
			return null;
		}
		
	}
	
	
}
