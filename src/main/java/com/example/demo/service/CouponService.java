package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.model.UserTableBean;
import com.example.demo.repository.UserRepository;

@Service
public class CouponService {

	private Logger logger = Logger.getLogger(CouponService.class.getName());
	private UserRepository userRepository;
	
	public CouponService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public boolean addOneCoupon(Long userId) {
		userRepository.findById(userId)
		.filter(user -> user.getCoupon() > 0)
		.map(user -> {
			user.setCoupon((byte)(user.getCoupon()+1));
			userRepository.save(user);
			return true;
		})
		.orElse(null);
		
		return false;
	}
	
	public boolean deleteOneCoupon(Long userId) {
		userRepository.findById(userId)
		.filter(user -> user.getCoupon() > 0)
		.map(user -> {
			user.setCoupon((byte)(user.getCoupon()-1));
			userRepository.save(user);
			return true;
		})
		.orElse(null);
		
		return false;
	}
	
	//秒（0-59） 分鐘（0-59） 小時（0-23） 日（1-31） 月（1-12） 星期（0-6，星期天是0）
	@Scheduled(cron = "0 0 10 1 * *")
	public void sendAnnualCoupons() {
		logger.info("執行發送周年優惠券");
		LocalDateTime currentDateStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
		LocalDateTime currentDateEnd = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);

		List<UserTableBean> users = userRepository.findUsersByCreateTime(currentDateStart, currentDateEnd);
		for(UserTableBean user : users) {
			sendCoupon(user);
		}
		
		userRepository.saveAll(users);
	}
	
	private void sendCoupon(UserTableBean user) {
		user.setCoupon((byte)(user.getCoupon()+1));
	}
}
