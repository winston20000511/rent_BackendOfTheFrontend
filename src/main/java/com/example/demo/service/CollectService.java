package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.HouseOwnerInfoDTO;
import com.example.demo.model.CollectTableBean;
import com.example.demo.model.HouseTableBean;
import com.example.demo.model.UserTableBean;
import com.example.demo.repository.CollectRepository;
import com.example.demo.repository.HouseRepository;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CollectService {

	@Autowired
	private CollectRepository collectRepository;
	@Autowired
	private UserRepository userRepository; // 假設您有一個 UserRepository

	@Autowired
	private HouseRepository houseRepository;
	@Transactional
	public void deleteByUserIdAndHouseId(Long userId, Long houseId) {
        collectRepository.deleteByUserIdAndHouseId(userId, houseId);
    }
	
	
	@Transactional
    public void addFavorite(Long userId, Long houseId) {
        Optional<UserTableBean> userOptional = userRepository.findById(userId);
        Optional<HouseTableBean> houseOptional = houseRepository.findById(houseId);

        if (userOptional.isPresent() && houseOptional.isPresent()) {
            CollectTableBean collect = new CollectTableBean();
            collect.setUser(userOptional.get());
            collect.setHouse(houseOptional.get());
            collect.setCollectTime(LocalDateTime.now());

            collectRepository.save(collect);
        } else {
            throw new IllegalArgumentException("User or House not found.");
        }
    }
	
	public boolean isHouseFavorited(Long userId, Long houseId) {
        return collectRepository.existsByUser_UserIdAndHouse_HouseId(userId, houseId);
    }



    public List<Long> getHouseIdsByUserId(Long userId) {
        return collectRepository.findHouseIdsByUserId(userId);
    }

}

