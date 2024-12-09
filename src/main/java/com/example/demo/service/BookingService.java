package com.example.demo.service;

import java.util.Optional;

import javax.print.attribute.standard.DateTimeAtCompleted;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BookingDTO;
import com.example.demo.dto.BookingDetailDTO;
import com.example.demo.dto.BookingSlotDTO;
import com.example.demo.dto.HouseOwnerDetailDTO;
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

	
	
	public BookingDetailDTO createBooking(BookingDTO booking) {
		System.out.println("000000000000000000000000");
		
		System.out.println(booking.getHouseId());
		System.out.println(booking.getBookingDate());
		System.out.println(booking.getFromTime());
		
		
		BookingBean bool = bookingRepo.isExistBooking(booking.getHouseId(), booking.getBookingDate(),
				booking.getFromTime());
		System.out.println(bool);
System.out.println("11111111111111111");
		BookingBean newBean = null;
		if (bool==null) {
			newBean = bookingRepo.save(convertToBean(booking));
			System.out.println("222222222222222");
		}
		
		System.out.println("3333333333");
		if(newBean!=null) {
			BookingDetailDTO bookingDetail = bookingRepo.findBookingDetailsById(newBean.getBookingId());
			System.out.println("44444444444");
	        sendEmailToOwner(bookingDetail);
	        return bookingDetail;
		}
		
		System.out.println("555555555555");
		return null;
	}

	private void sendEmailToOwner(BookingDetailDTO booking) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(booking.getOwnerEmail());
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

	private BookingDetailDTO convertToDetailDTO(BookingBean bean) {
		BookingDetailDTO dto = new BookingDetailDTO();
		dto.setOwnerName(bean.getHouse().getUser().getName());
		dto.setOwnerEmail(bean.getHouse().getUser().getEmail());
		dto.setUserName(null);
		dto.setUserEmail(null);
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
		bean.setCreateDate(dto.getCreateDate());
		bean.setBookingDate(dto.getBookingDate());
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
