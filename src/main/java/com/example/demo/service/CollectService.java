package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.CollectTableBean;
import com.example.demo.model.HouseTableBean;
import com.example.demo.model.UserTableBean;
import com.example.demo.repository.CollectRepository;
import com.example.demo.repository.HouseRepository;
import com.example.demo.repository.UserRepository;

@Service
public class CollectService {

	@Autowired
	private CollectRepository collectRepository;
	@Autowired
	private UserRepository userRepository; // 假設您有一個 UserRepository

	@Autowired
	private HouseRepository houseRepository;

	public void deleteByUserIdAndHouseId(Long userId, Long houseId) {
        collectRepository.deleteByUserIdAndHouseId(userId, houseId);
    }
	public List<Long> getHouseIdsByUserId(Long userId) {
		return collectRepository.findHouseIdsByUserId(userId);
	}

	public CollectTableBean addToCollection(CollectTableBean collect) {
	    // 檢查 house 是否已存在
	    if (collect.getHouse() == null || collect.getHouse().getHouseId() == null) {
	        throw new RuntimeException("House ID cannot be null");
	    }
	    
	    HouseTableBean existingHouse = houseRepository.findById(collect.getHouse().getHouseId())
	            .orElseThrow(() -> new RuntimeException("House does not exist"));
	    collect.setHouse(existingHouse); // 設置已存在的 house

	    // 檢查 user 是否已存在
	    if (collect.getUser() == null || collect.getUser().getUserId() == null) {
	        throw new RuntimeException("User ID cannot be null");
	    }
	    UserTableBean existingUser = userRepository.findById(collect.getUser().getUserId())
	            .orElseThrow(() -> new RuntimeException("User does not exist"));
	    collect.setUser(existingUser); // 設置已存在的 user

	    // 設置收藏時間
	    collect.setCollectTime(LocalDateTime.now());

	    // 保存 CollectTableBean
	    return collectRepository.save(collect);
	}
}

