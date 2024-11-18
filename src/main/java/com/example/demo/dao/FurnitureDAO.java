package com.example.demo.dao;

import java.util.Map;

import com.example.demo.model.FurnitureTableBean;

public interface FurnitureDAO {
	//Read
	FurnitureTableBean findFurnitureById(int houseId);
			
	//Create
		boolean createFurniture(FurnitureTableBean furnitureTB);
		
	//Update
		boolean updateFurniture(Map<String, Object> updates, int furnitureId);
		
	//Delete
		boolean deleteFurnitureById(int furnitureId);
}
