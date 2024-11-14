package com.example.demo.model;

import java.util.Map;

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
