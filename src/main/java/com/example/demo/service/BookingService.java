package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
	
	@Autowired
    private JavaMailSender mailSender;
	
	public HouseBookingTimeSlotBean findTimeSlotByHouseId(Long houseId) {
		Optional<HouseBookingTimeSlotBean> op = timeSlotRepo.findById(houseId);
		
		if(op.isPresent()) {
			return op.get();
		}else {
			return null;
		}
		
	}
	
	public HouseBookingTimeSlotBean updataTimeSlot(HouseBookingTimeSlotBean timeSlot) {
		return timeSlotRepo.save(timeSlot);
	}
	
	public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("your-email@example.com");
        mailSender.send(message);
    }
	
}
