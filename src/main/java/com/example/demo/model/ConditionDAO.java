package com.example.demo.model;

import java.util.Map;

public interface ConditionDAO {
	//Read
	ConditionTableBean findConditionById(int houseId);
				
		//Create
			boolean createCondition(ConditionTableBean conditionTB);
			
		//Update
			boolean updateCondition(Map<String, Object> updates,int conditionId);
			
		//Delete
			boolean deleteConditionById(int conditionId);
}
