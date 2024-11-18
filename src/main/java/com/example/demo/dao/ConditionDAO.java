package com.example.demo.dao;

import java.util.Map;

import com.example.demo.model.ConditionTableBean;

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
