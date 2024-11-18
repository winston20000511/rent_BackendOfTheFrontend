package com.example.demo.dao;

import java.util.Map;

import com.example.demo.model.HouseTableBean;

public interface HouseDAO {
	
	// Read
	HouseTableBean findHouseById(int houseId);
	


	// Create
	boolean createHouse(HouseTableBean houseTB);

	// Update
	boolean updateHouse(Map<String, Object> updates, int houseId);

	// Delete
	boolean deleteHouseById(int houseId);
}
