package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.dto.HouseDetailsDTO;
import com.example.demo.model.HouseImageTableBean;
import com.example.demo.model.HouseTableBean;

public interface HouseRepository extends JpaRepository<HouseTableBean, Long> {

	void save(HouseDetailsDTO house);
	
	 List<HouseImageTableBean> findByHouseId(Long houseId);
}