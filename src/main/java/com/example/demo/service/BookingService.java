package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BookingDTO;
import com.example.demo.dto.BookingSlotDTO;
import com.example.demo.model.BookingBean;
import com.example.demo.model.HouseBookingTimeSlotBean;
import com.example.demo.model.HouseTableBean;
import com.example.demo.model.UserTableBean;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.BookingTimeSlotRepository;
import com.example.demo.repository.HouseRepository;

@Service
public class BookingService {

	@Autowired
	private BookingRepository bookingRepo;

	@Autowired
	public BookingTimeSlotRepository timeSlotRepo;
	
	@Autowired
	private HouseRepository houseRepository;
	
	@Autowired
    private JavaMailSender mailSender;
	
	public BookingSlotDTO findTimeSlotByHouseId(Long houseId) {
		Optional<HouseBookingTimeSlotBean> op = timeSlotRepo.findById(houseId);
		return op.map(this::convertToDTO).orElse(null);
	}

	public BookingSlotDTO updataTimeSlot(HouseBookingTimeSlotBean timeSlot) {
		HouseBookingTimeSlotBean bean = timeSlotRepo.save(timeSlot);
		BookingSlotDTO dto = convertToDTO(bean);
		return dto;
	}
	
	
	public BookingDTO createBooking(BookingDTO booking) {
		;
		
        BookingBean newBooking = bookingRepo.save(convertToBean(booking));
        
        // 发送邮件给房子主人
        sendEmailToOwner(booking.getHouseId(), booking);
        
        return savedBooking;
    }
		
	private void sendEmailToOwner(Long houseId, BookingDTO booking) {
        // 获取房子主人的邮箱（假设有方法获取）
        String ownerEmail = houseRepository.getOwnerEmailByHouseId(houseId);
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(ownerEmail);
        message.setSubject("新的预约通知");
        message.setText("用户已预约从 " + booking.getFromTime() + " 到 " + booking.getToTime());
        
        mailSender.send(message);
    }

    

	
	private BookingDTO convertToDTO(BookingBean bean) {
		BookingDTO dto = new BookingDTO();
		dto.setHouseId(bean.getHouseId());
		dto.setUserId(bean.getUserId());
		dto.setBookingDate(bean.getBookingDate());
		dto.setFromTime(bean.getFromTime());
		dto.setToTime(bean.getToTime());
		dto.setStatus(bean.getStatus());
		return dto;
	}
	
	
	private BookingSlotDTO convertToDTO(HouseBookingTimeSlotBean bean) {
		BookingSlotDTO dto = new BookingSlotDTO();
		dto.setHouseId(bean.getHouseId());
		dto.setFromDate(bean.getFromDate());
		dto.setToDate(bean.getToDate());
		dto.setFromTime(bean.getFromTime());
		dto.setToTime(bean.getToTime());
		dto.setDuration(bean.getDuration());
		dto.setWeekDay(bean.getWeekDay());
		return dto;
	}
	
	private BookingBean convertToBean(BookingDTO dto) {
	    BookingBean bean = new BookingBean();
	    bean.setHouseId(dto.getHouseId());
	    bean.setUserId(dto.getUserId());
	    bean.setBookingDate(dto.getBookingDate()); // 假设 BookingDTO 中有这个字段
	    bean.setFromTime(dto.getFromTime());
	    bean.setToTime(dto.getToTime());
	    bean.setStatus(dto.getStatus());
	    return bean;
	}
	
	private HouseBookingTimeSlotBean convertToBean(BookingSlotDTO dto) {
	    HouseBookingTimeSlotBean bean = new HouseBookingTimeSlotBean();
	    bean.setHouseId(dto.getHouseId());
	    bean.setFromDate(dto.getFromDate());
	    bean.setToDate(dto.getToDate());
	    bean.setFromTime(dto.getFromTime());
	    bean.setToTime(dto.getToTime());
	    bean.setDuration(dto.getDuration());
	    bean.setWeekDay(dto.getWeekDay());
	    return bean;
	}
	
	public boolean confirm(Long houseId, Long userId) {
		Optional<HouseTableBean> house = houseRepository.findById(houseId);

		if (house.isPresent()) {
			UserTableBean landlord = house.get().getUser();

			if (landlord != null) {
				return landlord.getUserId().equals(userId);
			}
		}

		return false;
	}
}
