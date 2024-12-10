package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.CollectRepository;

@Service
public class CollectService {

	@Autowired
    private CollectRepository collectRepository;

	 public void deleteCollectByHouseId(Long houseId) {
	        collectRepository.deleteByHouseId(houseId);
	    }
	 
	 public List<Long> getHouseIdsByUserId(Long userId) {
	        return collectRepository.findHouseIdsByUserId(userId);
	    }
	 
	 
}
