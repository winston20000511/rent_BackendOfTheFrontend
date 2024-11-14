package com.example.demo.model;

import java.util.Map;

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
