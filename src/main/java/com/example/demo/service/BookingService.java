package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BookingDTO;
import com.example.demo.dto.BookingDetailDTO;
import com.example.demo.dto.BookingListDTO;
import com.example.demo.dto.BookingOwnerListDTO;
import com.example.demo.dto.BookingResponseDTO;
import com.example.demo.dto.BookingSlotDTO;
import com.example.demo.model.BookingBean;
import com.example.demo.model.HouseBookingTimeSlotBean;
import com.example.demo.model.HouseTableBean;
import com.example.demo.model.MessageBean;
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
	
	@Autowired
    private MessageService messageService;
	
	// 預設前台會員中心網址
	protected String url = "http://localhost:5173/member-center" ;
	
	// 取得當下時間
	protected LocalDateTime getCurrentTime() {
	    return LocalDateTime.now();
	}
	
	// 更新 預約狀態
	@Scheduled(cron = "0 1 0 * * ?")//每天 01:01 更新
    public void updateHouseStatus() {
		System.out.println("-------開始更新預約狀態---------");

		LocalDate yesterday = LocalDate.now().plusDays(1);
        bookingRepo.updateStatusOfOverdue(yesterday);
        bookingRepo.updateStatusOfFinish(yesterday);
        
        System.out.println("-----------結束--------------");
    }
	

	public List<String> findBookingedByHouseId(Long houseId){
		List<String> bookingedList = bookingRepo.findBookingedByHouseId(houseId);
		return bookingedList;
	}
	
	public BookingSlotDTO findTimeSlotByHouseId(Long houseId) {
		Optional<HouseBookingTimeSlotBean> op = timeSlotRepo.findById(houseId);
		return op.map(this::convertToDTO).orElse(null);
	}

	public BookingSlotDTO updataTimeSlot(HouseBookingTimeSlotBean timeSlot) {
		HouseBookingTimeSlotBean bean = timeSlotRepo.save(timeSlot);
		BookingSlotDTO dto = convertToDTO(bean);
		return dto;
	}
	
	// 使用者的預約清單
	public List<BookingListDTO> getBookingByUser(Long userId) {
		
		return bookingRepo.findBookingListByUserId(userId);
	}

	// 使用者的房屋預約清單
	public List<BookingOwnerListDTO> getBookingByHouseOwner(Long userId) {
		
		return bookingRepo.findByHouseOwnerUserId(userId);
	}
	
	// 新建預約
	public BookingResponseDTO createBooking(BookingDTO booking) throws MessagingException {
		Integer bool = bookingRepo.isExistBooking(booking.getHouseId(), booking.getBookingDate(),
				booking.getBookingTime());
		BookingBean newBean = null;
		
		// 檢查 有無重複預約
		if (bool != null && bool > 0) {
			return new BookingResponseDTO("danger", "預約失敗: 該時段已被預約!");
		} else {
			newBean = bookingRepo.save(convertToBean(booking));
			if (newBean != null) {
				BookingDetailDTO bookingDetail = bookingRepo.findBookingDetailsById(newBean.getBookingId());
				
				String formattedMessage = booking.getMessage()
		                .replace("<", "&lt;")
		                .replace(">", "&gt;")
		                .replace("\n", "<br/>");
				
				String sendMailMSG = "<div style='width:640px; border: 1px solid #007bff; border-radius: 10px; padding: 15px;'>" +
			             "<h2 style='text-align: left ;'>您在 <span style='color:red;'>" + 
			             bookingDetail.getBookingDate() + " " + bookingDetail.getBookingTime() + "</span> 有新的預約</h2>" +
			             "<span style='text-align: center;'>立即查看： </span>" +
			             "<span style='text-align: center;'><a href='"+ url +"/homeowner' style='color: blue;'>"+url+"/homeowner</a></span>" +
			             "<h3 style='text-align: start;'>給您的留言:</h3>" +
			             "<div style='border: 1px solid #ccc; padding: 10px; margin-top: 15px;'>" +
			             "<p style='text-align: start;'>" + formattedMessage + "</p>" +
			             "</div>" +
			             "<hr style='border-top: 1px solid #ccc;'>" +
			             "<footer style='text-align: center; font-size: small; color: gray;'><p>如有任何問題，請聯絡客服：rent189.customer.service@gmail.com</p><p>© 2024 189租屋網 版權所有</p></footer>" +
			             "</div>";

				sendSimpleEmail(bookingDetail.getHouseOwnerEmail(), "《通知》您有新的預約", sendMailMSG);
				
				// 寄Mail
				MessageBean msgBean = new MessageBean();
				MessageBean msgBean2 = new MessageBean();
				String sendChatRoomMSG ="我在 "+bookingDetail.getBookingDate()+" "+bookingDetail.getBookingTime()+" 有一個預約。";
//				System.out.println(sendChatRoomMSG);

				// 發站內訊息
				msgBean.setSenderId(booking.getUserId());				//預約者ID
				msgBean.setReceiverId(bookingDetail.getHouseOwnerId());	//房東ID	
				msgBean.setMessage(sendChatRoomMSG);
				msgBean.setTimestamp(getCurrentTime());
				messageService.saveMessage(msgBean);
				
				// 預約者有留言時，則發第二條訊息
				if(!booking.getMessage().isEmpty()) {
					msgBean2.setSenderId(booking.getUserId());	
					msgBean2.setReceiverId(bookingDetail.getHouseOwnerId());	//房東ID	
					msgBean2.setMessage(booking.getMessage());
					msgBean2.setTimestamp(getCurrentTime());
					messageService.saveMessage(msgBean2);

				}
				
				return new BookingResponseDTO("success", "預約已送出!");
			} else {

				return new BookingResponseDTO("danger", "預約失敗!");
			}
		}
	}
	
	// 屋主更改預約狀態 (1: 屋主同意 ; 2: 屋主拒絕 ; 3:屋主取消)
	public BookingResponseDTO updateBookingByHost(BookingDTO booking) throws MessagingException {
		Optional<BookingBean> op = bookingRepo.findById(booking.getBookingId());

		BookingBean newBean = new BookingBean();
		if (op.isPresent()) {
			newBean = op.get();
			newBean.setStatus(booking.getStatus());
			BookingBean b = bookingRepo.save(newBean);
			BookingDetailDTO bookingDetail = bookingRepo.findBookingDetailsById(b.getBookingId());
			
			String text ="";
			Byte status = bookingDetail.getStatus();
			
			switch (status){
				case 1 : text = "同意"; break;
				case 2 : text = "拒絕"; break;
				case 3 : text = "取消"; break;
				default: return new BookingResponseDTO("danger", "無效的預約!");
			}
			
			// 寄Mail
			String sendMailMSG = "<div style='width:640px; border: 1px solid #007bff; border-radius: 10px; padding: 15px;'>" +
		             "<h2 style='text-align: left ;'>您在 <span style='color:red;'>" + 
		             b.getBookingDate() + " " + b.getBookingTime() + "</span> 的預約已"+text+"</h2>" +
		             "<span style='text-align: center;'>立即查看： </span>" +
		             "<span style='text-align: center;'><a href='"+ url +"/my-booking' style='color: blue;'>"+url+"/my-booking</a></span>" +
		             "<hr style='border-top: 1px solid #ccc;'>" +
		             "<footer style='text-align: center; font-size: small; color: gray;'><p>© 2024 189租屋網 版權所有</p></footer>" +
		             "</div>";
			sendSimpleEmail(bookingDetail.getUserEmail(), "《通知》預約已"+text, sendMailMSG);
			
			// 發站內訊息
			String sendChatRoomMSG = "預約時間: "+bookingDetail.getBookingDate()+" "+bookingDetail.getBookingTime()+"，此次預約已"+text+"!。敬請留意。";
			MessageBean msgBean = new MessageBean();
			msgBean.setSenderId(bookingDetail.getHouseOwnerId());	//房東ID(寄信者)
			msgBean.setReceiverId(booking.getUserId());			//預約者ID(收信者)
			msgBean.setMessage(sendChatRoomMSG);
			msgBean.setTimestamp(getCurrentTime());
			messageService.saveMessage(msgBean);
			
			return new BookingResponseDTO("success", "操作成功!");
		}
		return new BookingResponseDTO("danger", "操作失敗!");
	}
	
	// 用戶更改預約狀態
	public BookingResponseDTO cancelBookingByGuest(BookingDTO booking) throws MessagingException {
		Optional<BookingBean> op = bookingRepo.findById(booking.getBookingId());

		BookingBean newBean = new BookingBean();
		if (op.isPresent()) {
			newBean = op.get();
			newBean.setStatus(booking.getStatus());
			BookingBean b = bookingRepo.save(newBean);
			BookingDetailDTO bookingDetail = bookingRepo.findBookingDetailsById(b.getBookingId());

			// 寄Mail
			String sendMailMSG = "<div style='width:640px; border: 1px solid #007bff; border-radius: 10px; padding: 15px;'>" +
		             "<h2 style='text-align: left ;'>您在 <span style='color:red;'>" + 
		             b.getBookingDate() + " " + b.getBookingTime() + "</span> 的預約已被取消</h2>" +
		             "<span style='text-align: center;'>立即查看： </span>" +
		             "<span style='text-align: center;'><a href='"+ url +"/homeowner' style='color: blue;'>"+url+"/homeowner</a></span>" +
		             "<hr style='border-top: 1px solid #ccc;'>" +
		             "<footer style='text-align: center; font-size: small; color: gray;'><p>如有任何問題，請聯絡客服：rent189.customer.service@gmail.com</p><p>© 2024 189租屋網 版權所有</p></footer>" +
		             "</div>";
			sendSimpleEmail(bookingDetail.getHouseOwnerEmail(), "《通知》預約已取消", sendMailMSG);
			
			// 發站內訊息
			String sendChatRoomMSG = "預約時間: "+bookingDetail.getBookingDate()+" "+bookingDetail.getBookingTime()+"，此次預約已取消!。敬請留意。";
			MessageBean msgBean = new MessageBean();
			msgBean.setSenderId(bookingDetail.getHouseOwnerId());	//預約者ID(收信者)
			msgBean.setReceiverId(booking.getUserId());				//房東ID(寄信者)
			msgBean.setMessage(sendChatRoomMSG);
			msgBean.setTimestamp(getCurrentTime());
			messageService.saveMessage(msgBean);
			
			return new BookingResponseDTO("success", "已完成取消!");
		}
		
		return new BookingResponseDTO("danger", "取消失敗!");
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

		dto.setHouseOwnerName(bean.getHouse().getUser().getName());
		dto.setHouseOwnerEmail(bean.getHouse().getUser().getEmail());
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
