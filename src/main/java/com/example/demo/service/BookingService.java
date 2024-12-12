package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BookingDTO;
import com.example.demo.dto.BookingDetailDTO;
import com.example.demo.dto.BookingSlotDTO;
import com.example.demo.model.BookingBean;
import com.example.demo.model.HouseBookingTimeSlotBean;
import com.example.demo.model.HouseTableBean;
import com.example.demo.model.UserTableBean;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.BookingTimeSlotRepository;
import com.example.demo.repository.HouseRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

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

	public List<BookingDTO> getBookingByUser(Long userId) {
		List<BookingBean> bs = bookingRepo.findByUserId(userId);
		List<BookingDTO> dto = new ArrayList<>();

		if (!bs.isEmpty()) {
			for (BookingBean b : bs) {
				BookingDTO d = convertToDTO(b);
				dto.add(d);
			}
		}
		return dto;
	}

	public List<BookingDTO> getBookingByHouse(Long houseId) {
		List<BookingBean> bs = bookingRepo.findByHouseId(houseId);
		List<BookingDTO> dto = new ArrayList<>();

		if (!bs.isEmpty()) {
			for (BookingBean b : bs) {
				BookingDTO d = convertToDTO(b);
				dto.add(d);
			}
		}
		return dto;
	}

	public String createBooking(BookingDTO booking) throws MessagingException {
		Integer bool = bookingRepo.isExistBooking(booking.getHouseId(), booking.getBookingDate(),
				booking.getBookingTime());
		BookingBean newBean = null;

		if (bool != null && bool > 0) {
			return "預約失敗: 該時段已被預約!";
		} else {
			newBean = bookingRepo.save(convertToBean(booking));
			if (newBean != null) {
				BookingDetailDTO b = bookingRepo.findBookingDetailsById(newBean.getBookingId());

				String msg = "<h2>您在 <span style='color:red;'>" + b.getBookingDate() + " " + b.getBookingTime()
						+ "</span> 有新的預約</h2>" + "<br/>http://localhost:8080/";

				sendSimpleEmail(b.getOwnerEmail(), "您有新的預約", msg);
				return "預約成功!";
			} else {

				return "預約失敗";
			}
		}
	}

	public BookingDTO updateBookingByHost(BookingDTO booking) {
		Optional<BookingBean> op = bookingRepo.findById(booking.getBookingId());

		BookingBean newBean = new BookingBean();
		if (op.isPresent()) {
			newBean = op.get();
			newBean.setStatus(booking.getStatus());
		}

		return convertToDTO(bookingRepo.save(newBean));
	}

	public BookingDTO updateBookingByGuest(BookingDTO booking) {
		Optional<BookingBean> op = bookingRepo.findById(booking.getBookingId());

		BookingBean newBean = new BookingBean();
		if (op.isPresent()) {
			newBean = op.get();
			newBean.setStatus(booking.getStatus());
		}

		return convertToDTO(bookingRepo.save(newBean));
	}

	private void sendSimpleEmail(String to, String subject, String text) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text, true);
		mailSender.send(message);
	}

	private BookingDTO convertToDTO(BookingBean bean) {
		BookingDTO dto = new BookingDTO();
		dto.setBookingId(bean.getBookingId());
		dto.setHouseId(bean.getHouseId());
		dto.setUserId(bean.getUserId());
		dto.setCreateDate(bean.getCreateDate());
		dto.setBookingDate(bean.getBookingDate());
		dto.setBookingTime(bean.getBookingTime());
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
		dto.setBookingTime(bean.getBookingTime());
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
		bean.setBookingId(dto.getBookingId());
		bean.setHouseId(dto.getHouseId());
		bean.setUserId(dto.getUserId());
		bean.setCreateDate(dto.getCreateDate());
		bean.setBookingDate(dto.getBookingDate());
		bean.setBookingTime(dto.getBookingTime());
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
