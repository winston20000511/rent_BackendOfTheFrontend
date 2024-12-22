package com.example.demo.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.HouseDetailsDTO;
import com.example.demo.dto.HouseListByUserIdDTO;
import com.example.demo.dto.HouseOwnerInfoDTO;
import com.example.demo.model.ConditionTableBean;
import com.example.demo.model.FurnitureTableBean;
import com.example.demo.model.HouseImageTableBean;
import com.example.demo.model.HouseTableBean;
import com.example.demo.repository.ConditionRepository;
import com.example.demo.repository.FurnitureRepository;
import com.example.demo.repository.HouseImageRepository;
import com.example.demo.repository.HouseRepository;

import jakarta.transaction.Transactional;

@Service
public class HouseService {

	@Autowired
	private HouseRepository houseRepository;

	@Autowired
	private FurnitureRepository furnitureRepository;

	@Autowired
	private ConditionRepository conditionRepository;
	
	@Autowired
	private HouseImageRepository houseImageRepository;
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
		dto.setRoom(house.getRoom());
		dto.setBathroom(house.getBathroom());
		dto.setLivingroom(house.getLivingroom());
		dto.setKitchen(house.getKitchen());
		dto.setFloor(house.getFloor());
		dto.setHouseType(house.getHouseType());
		dto.setAtticAddition(house.getAtticAddition());
		dto.setStatus(house.getStatus());
		dto.setDescription(house.getDescription());

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
		dto.setChannel4(furniture.getChannel4());
		dto.setElevator(condition.getElevator());
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

	public HouseTableBean addHouse(HouseTableBean house) {

		return houseRepository.save(house);
	}




	public List<Map<String, Object>> findNoAdHousesByUserId(Long userId) {
		return houseRepository.findNoAdHouses(userId);
	}
	public List<HouseListByUserIdDTO> getHousesByUserId(Long userId) {
	    // 使用 findHouseByUserId 方法查詢結果
	    List<HouseListByUserIdDTO> houses = houseRepository.findHousesByUserId(userId);
	    
	    // 返回查詢結果
	    return houses;
	}
	 @Transactional
	    public void updateHouse(Long houseId, Map<String, String> houseData, 
	                            Map<String, Boolean> furnitureServices, 
	                            Map<String, Boolean> houseRestrictions, 
	                            List<MultipartFile> newImages, 
	                            List<Long> existingImageIds) throws IOException {
	        // 1. 檢查房屋是否存在
	        HouseTableBean house = houseRepository.findById(houseId)
	                .orElseThrow(() -> new RuntimeException("房屋不存在"));

	        // 2. 更新房屋基本資料
	        house.setTitle(houseData.get("title"));
	        house.setPrice(Integer.valueOf(houseData.get("price")));
	        house.setSize(Integer.valueOf(houseData.get("size")));
	        house.setAddress(houseData.get("address"));
	        house.setRoom(Byte.valueOf(houseData.get("room")));
	        house.setBathroom(Byte.valueOf(houseData.get("bathroom")));
	        house.setDescription(houseData.get("description"));
	        houseRepository.save(house);

	        // 3. 更新房屋限制條件
	        ConditionTableBean condition = conditionRepository.findById(houseId)
	                .orElse(new ConditionTableBean());
	        condition.setHouse(house);
	        condition.setPet(houseRestrictions.getOrDefault("pet", false));
	        condition.setParkingSpace(houseRestrictions.getOrDefault("parkingSpace", false));
	        condition.setElevator(houseRestrictions.getOrDefault("elevator", false));
	        condition.setBalcony(houseRestrictions.getOrDefault("balcony", false));
	        condition.setShortTerm(houseRestrictions.getOrDefault("shortTerm", false));
	        condition.setCooking(houseRestrictions.getOrDefault("cooking", false));
	        condition.setWaterDispenser(houseRestrictions.getOrDefault("waterDispenser", false));
	        condition.setManagementFee(houseRestrictions.getOrDefault("managementFee", false));
	        condition.setGenderRestrictions(Byte.valueOf(houseData.getOrDefault("genderRestrictions", "0")));
	        conditionRepository.save(condition);

	        // 4. 更新家具信息
	        FurnitureTableBean furniture = furnitureRepository.findById(houseId)
	                .orElse(new FurnitureTableBean());
	        furniture.setHouse(house);
	        furniture.setWashingMachine(furnitureServices.getOrDefault("washingMachine", false));
	        furniture.setAirConditioner(furnitureServices.getOrDefault("airConditioner", false));
	        furniture.setNetwork(furnitureServices.getOrDefault("network", false));
	        furniture.setBedstead(furnitureServices.getOrDefault("bedstead", false));
	        furniture.setMattress(furnitureServices.getOrDefault("mattress", false));
	        furniture.setRefrigerator(furnitureServices.getOrDefault("refrigerator", false));
	        furniture.setEwaterHeater(furnitureServices.getOrDefault("ewaterHeater", false));
	        furniture.setGwaterHeater(furnitureServices.getOrDefault("gwaterHeater", false));
	        furniture.setTelevision(furnitureServices.getOrDefault("television", false));
	        furniture.setChannel4(furnitureServices.getOrDefault("channel4", false));
	        furniture.setSofa(furnitureServices.getOrDefault("sofa", false));
	        furniture.setTables(furnitureServices.getOrDefault("tables", false));
	        furnitureRepository.save(furniture);

	        // 5. 更新圖片
	        // 5.1 刪除未保留的圖片
	        List<HouseImageTableBean> existingImages = houseImageRepository.findByHouseId(houseId);
	        List<HouseImageTableBean> imagesToDelete = existingImages.stream()
	                .filter(image -> !existingImageIds.contains(image.getId()))
	                .collect(Collectors.toList());
	        houseImageRepository.deleteAll(imagesToDelete);

	        // 5.2 儲存新圖片
	        for (MultipartFile file : newImages) {
	            HouseImageTableBean newImage = new HouseImageTableBean();
	            newImage.setHouse(house);
	            newImage.setImages(file.getBytes());
	            houseImageRepository.save(newImage);
	        }
	    }
	 public boolean updateHouseStatusToTwo(Long houseId) {
	        Optional<HouseTableBean> optionalHouse = houseRepository.findById(houseId);
	        if (optionalHouse.isPresent()) {
	            HouseTableBean house = optionalHouse.get();
	            // 檢查當前狀態是否為0或1
	            if (house.getStatus() == 0 || house.getStatus() == 1) {//待審核:0 啟動中:1
	                house.setStatus((byte) 2); // 更新狀態為2
	                houseRepository.save(house);
	                return true; // 更新成功
	            }
	        }
	        return false; // 房屋不存在或當前狀態不是0或1
	    }

public HouseOwnerInfoDTO getHouseOwnerInfoByHouseId(Long houseId) {
    return houseRepository.findHouseOwnerInfoByHouseId(houseId);
}
	
}
