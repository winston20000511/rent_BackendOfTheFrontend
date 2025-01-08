package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.model.UserTableBean;
import com.example.demo.repository.UserRepository;

@Service
public class CouponService {

	private Logger logger = Logger.getLogger(CouponService.class.getName());
	private final UserRepository userRepository;
	private final EntityManager entityManager;
	
	public CouponService(UserRepository userRepository, EntityManager entityManager) {
		this.userRepository = userRepository;
		this.entityManager = entityManager;
	}

	@Transactional
	public boolean addOneCoupon(Long userId) {
		UserTableBean user = entityManager.find(UserTableBean.class, userId, LockModeType.PESSIMISTIC_WRITE);
		if(user == null){
			throw new RuntimeException("User not found");
		}

		if(user.getCoupon() > 0){
			user.setCoupon((byte) (user.getCoupon() + 1));
			userRepository.save(user);
			return true;
		}
		return false;
	}

	@Transactional
	public boolean deleteOneCoupon(Long userId) {
		UserTableBean user = entityManager.find(UserTableBean.class, userId, LockModeType.PESSIMISTIC_WRITE);
		if(user == null){
			throw new RuntimeException("User not found");
		}

		if(user.getCoupon() > 0){
			user.setCoupon((byte) (user.getCoupon() - 1));
			userRepository.save(user);
			return true;
		}
		return false;
	}

}
