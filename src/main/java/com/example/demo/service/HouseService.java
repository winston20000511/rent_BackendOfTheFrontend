package com.example.demo.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.demo.dto.HouseDetailsDTO;
import com.example.demo.dto.HouseListByUserIdDTO;
import com.example.demo.model.ConditionTableBean;
import com.example.demo.model.FurnitureTableBean;
import com.example.demo.model.HouseImageTableBean;
import com.example.demo.model.HouseTableBean;
import com.example.demo.repository.CollectRepository;
import com.example.demo.repository.ConditionRepository;
import com.example.demo.repository.FurnitureRepository;
import com.example.demo.repository.HouseImageRepository;
import com.example.demo.repository.HouseRepository;

@Service
public class HouseService {

	@Autowired
	private HouseImageRepository imageRepository;

	@Autowired
	private CollectRepository collectRepository;
	@Autowired
	private HouseRepository houseRepository;

	@Autowired
	private FurnitureRepository furnitureRepository;

	@Autowired
	private ConditionRepository conditionRepository;

	public HouseDetailsDTO getHouseDetails(Long houseId) {
		// 查詢 HouseTable
		HouseTableBean house = houseRepository.findById(houseId)
				.orElseThrow(() -> new RuntimeException("House not found"));

		// 查詢 FurnitureTable
		FurnitureTableBean furniture = furnitureRepository.findById(houseId).orElse(new FurnitureTableBean());

		// 查詢 ConditionTable
		ConditionTableBean condition = conditionRepository.findById(houseId).orElse(new ConditionTableBean());

		// 組裝 DTO
		HouseDetailsDTO dto = new HouseDetailsDTO();
		dto.setHouseId(house.getHouseId());
		dto.setTitle(house.getTitle());
		dto.setPrice(house.getPrice());
		dto.setSize(house.getSize());
		dto.setAddress(house.getAddress());

		// 設置 Furniture 信息
		dto.setWashingMachine(furniture.getWashingMachine());
		dto.setAirConditioner(furniture.getAirConditioner());
		dto.setNetwork(furniture.getNetwork());
		dto.setBedstead(furniture.getBedstead());
		dto.setMattress(furniture.getMattress());
		dto.setRefrigerator(furniture.getRefrigerator());
		dto.setEwaterHeater(furniture.getEwaterHeater());
		dto.setGwaterHeater(furniture.getGwaterHeater());
		dto.setTelevision(furniture.getTelevision());
		dto.setSofa(furniture.getSofa());
		dto.setTables(furniture.getTables());

		// 設置 Condition 信息
		dto.setPet(condition.getPet());
		dto.setParkingSpace(condition.getParkingSpace());
		dto.setBalcony(condition.getBalcony());
		dto.setShortTerm(condition.getShortTerm());
		dto.setCooking(condition.getCooking());
		dto.setWaterDispenser(condition.getWaterDispenser());
		dto.setManagementFee(condition.getManagementFee());
		dto.setGenderRestrictions(condition.getGenderRestrictions());

		return dto;
	}
	
    public String updateHouse(Long houseId, HouseDetailsDTO detail) {
        HouseTableBean house = houseRepository.findById(houseId)
				.orElseThrow(() -> new RuntimeException("House not found"));
        if (house == null) {
            return "房屋不存在";
        }


		// 查詢 FurnitureTable
		FurnitureTableBean furniture = furnitureRepository.findById(houseId).orElse(new FurnitureTableBean());

		// 查詢 ConditionTable
		ConditionTableBean condition = conditionRepository.findById(houseId).orElse(new ConditionTableBean());

		// 組裝 DTO
		HouseDetailsDTO dto = new HouseDetailsDTO();
		dto.setHouseId(house.getHouseId());
		dto.setTitle(house.getTitle());
		dto.setPrice(house.getPrice());
		dto.setSize(house.getSize());
		dto.setAddress(house.getAddress());

		// 設置 Furniture 信息
		dto.setWashingMachine(furniture.getWashingMachine());
		dto.setAirConditioner(furniture.getAirConditioner());
		dto.setNetwork(furniture.getNetwork());
		dto.setBedstead(furniture.getBedstead());
		dto.setMattress(furniture.getMattress());
		dto.setRefrigerator(furniture.getRefrigerator());
		dto.setEwaterHeater(furniture.getEwaterHeater());
		dto.setGwaterHeater(furniture.getGwaterHeater());
		dto.setTelevision(furniture.getTelevision());
		dto.setSofa(furniture.getSofa());
		dto.setTables(furniture.getTables());

		// 設置 Condition 信息
		dto.setPet(condition.getPet());
		dto.setParkingSpace(condition.getParkingSpace());
		dto.setBalcony(condition.getBalcony());
		dto.setShortTerm(condition.getShortTerm());
		dto.setCooking(condition.getCooking());
		dto.setWaterDispenser(condition.getWaterDispenser());
		dto.setManagementFee(condition.getManagementFee());
		dto.setGenderRestrictions(condition.getGenderRestrictions());

        
        houseRepository.save(house); // 更新房屋到資料庫
        return "房屋資訊已成功更新";
    }
    
	public HouseTableBean addHouse(HouseTableBean house) {

		return houseRepository.save(house);
	}

	public List<String> getHouseImagesByHouseId(Long houseId) {
		List<HouseImageTableBean> images = imageRepository.findByHouseId(houseId);

		return images.stream().map(this::convertImageToBase64).collect(Collectors.toList());
	}

	// 將byte[]轉換為Base64編碼的字串
	private String convertImageToBase64(HouseImageTableBean image) {
		if (image.getImageUrl() == null) {
			return null;
		}
		return Base64.getEncoder().encodeToString(image.getImageUrl());
	}

	public void deleteCollectByHouseId(Long houseId) {
		collectRepository.deleteByHouseId(houseId);
	}

	public Page<Map<String, Object>> findNoAdHousesByUserId(Pageable pageable) {
		return houseRepository.findHousesWithoutAds(pageable);
	}

	public List<HouseListByUserIdDTO> getHousesByUserId(Long userId) {
	    // 使用 findHouseByUserId 方法查詢結果
	    List<HouseListByUserIdDTO> houses = houseRepository.findHousesByUserId(userId);
	    
	    // 返回查詢結果
	    return houses;
	}
}
