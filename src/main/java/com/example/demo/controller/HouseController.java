package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.HouseDetailsDTO;
import com.example.demo.model.ConditionTableBean;
import com.example.demo.model.FurnitureTableBean;
import com.example.demo.model.HouseImageTableBean;
import com.example.demo.model.HouseTableBean;
import com.example.demo.model.UserTableBean;
import com.example.demo.service.CollectService;
import com.example.demo.service.HouseService;



@RestController
@RequestMapping("/api/houses")
public class HouseController {
	@Autowired
	private HouseService houseService;
	
	@Autowired
	private CollectService collectService;
	
    @SuppressWarnings("unchecked")
	@PostMapping("/add")
    public String addHouse(@RequestBody HouseDetailsDTO detail ) {
        // 在這裡處理接收到的數據，例如保存到資料庫
    	
    	UserTableBean user = new UserTableBean();
        user.setUserId((long) 1);
    	
    	HouseTableBean house = new HouseTableBean();
    	house.setUser(user);
        house.setTitle(detail.getTitle());
        house.setPrice(detail.getPrice());
        house.setSize(detail.getSize());
        house.setAddress(detail.getAddress());
        house.setRoom(detail.getRoom());
        house.setBathroom(detail.getBathroom());
        house.setLivingroom(detail.getLivingroom());
        house.setKitchen(detail.getKitchen());
        house.setFloor(detail.getFloor());
        house.setAtticAddition(detail.getAtticAddition());
        house.setStatus((byte) 0);
        house.setClickCount(0);
        // 設置家具與服務
        FurnitureTableBean furniture = new FurnitureTableBean();
        furniture.setWashingMachine(detail.getWashingMachine());
        furniture.setAirConditioner(detail.getAirConditioner());
        furniture.setNetwork(detail.getNetwork());
        furniture.setBedstead(detail.getBedstead());
        furniture.setMattress(detail.getMattress());
        furniture.setRefrigerator(detail.getRefrigerator());
        furniture.setEwaterHeater(detail.getEwaterHeater());
        furniture.setGwaterHeater(detail.getGwaterHeater());
        furniture.setTelevision(detail.getTelevision());
        furniture.setChannel4(detail.getChannel4());
        furniture.setSofa(detail.getSofa());
        furniture.setTables(detail.getTables());
        furniture.setHouse(house);
        house.setFurniture(furniture);

        // 設置房屋限制
        ConditionTableBean condition = new ConditionTableBean();
        condition.setPet(detail.getPet());
        condition.setParkingSpace(detail.getParkingSpace());
        condition.setElevator(detail.getElevator());
        condition.setBalcony(detail.getBalcony());
        condition.setShortTerm(detail.getShortTerm());
        condition.setCooking(detail.getCooking());
        condition.setWaterDispenser(detail.getWaterDispenser());
        condition.setManagementFee(detail.getManagementFee());
        condition.setGenderRestrictions(detail.getGenderRestrictions()); // 性別限制設置
        condition.setHouse(house);
        house.setCondition(condition);
      
        HouseImageTableBean image =new HouseImageTableBean();
        image.setImageUrl(detail.getImageUrl());
        image.setHouse(house);
        image.setUser(user);
        house.setImages((List<HouseImageTableBean>) image);
       // 處理圖片上傳邏輯（例如儲存圖片）
        
        houseService.addHouse(house);
       return "房屋資訊已成功儲存";
   }
    
    
    @GetMapping("/getPhotos/{houseId}")
    public ResponseEntity<List<String>> getHousePhotos(@PathVariable Long houseId) {
        List<String> base64Images = houseService.getHouseImagesByHouseId(houseId);
        
        if (base64Images.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(base64Images);
    }
    
    @GetMapping("/details/{houseId}")
    public ResponseEntity<HouseDetailsDTO> getHouseDetails(@PathVariable Long houseId) {
        HouseDetailsDTO houseDetails = houseService.getHouseDetails(houseId);
        return ResponseEntity.ok(houseDetails);
    }
    
    @DeleteMapping("/deleteCollecthouse/{houseId}")
    public ResponseEntity<String> deleteCollectByHouseId(@PathVariable Long houseId) {
        try {
            houseService.deleteCollectByHouseId(houseId);
            return ResponseEntity.ok("Collect data deleted successfully for houseId: " + houseId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to delete collect data: " + e.getMessage());
        }
    }
    
    @GetMapping("/collect/{userId}")
    public List<Long> getHouseIds(@PathVariable Long userId) {
        return collectService.getHouseIdsByUserId(userId);
    }
    
    
    
    
    
    
    
    
    
    
    
    
}
