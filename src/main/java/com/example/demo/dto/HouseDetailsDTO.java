package com.example.demo.dto;

import java.util.List;

import com.example.demo.model.ConditionTableBean;
import com.example.demo.model.FurnitureTableBean;

public class HouseDetailsDTO {
	
	private Long houseId;
    private List<FurnitureTableBean> furnitureList;
    private List<ConditionTableBean> houseRestrictionsList;
  
    public Long getHouseId() {
		return houseId;
	}
	public void setHouseId(Long houseId) {
		this.houseId = houseId;
	}
	public List<FurnitureTableBean> getFurnitureList() {
		return furnitureList;
	}
	public void setFurnitureList(List<FurnitureTableBean> furnitureList) {
		this.furnitureList = furnitureList;
	}
	public List<ConditionTableBean> getHouseRestrictionsList() {
		return houseRestrictionsList;
	}
	public void setHouseRestrictionsList(List<ConditionTableBean> houseRestrictionsList) {
		this.houseRestrictionsList = houseRestrictionsList;
	}


}
