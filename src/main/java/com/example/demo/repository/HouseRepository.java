package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.dto.HouseDetailsDTO;
import com.example.demo.model.HouseTableBean;

public interface HouseRepository extends JpaRepository<HouseTableBean, Long> {

	void save(HouseDetailsDTO house);
	
}