package com.example.demo.controller;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.HouseDetailsDTO;
import com.example.demo.dto.HouseListByUserIdDTO;
import com.example.demo.model.ConditionTableBean;
import com.example.demo.model.FurnitureTableBean;
import com.example.demo.model.HouseImageTableBean;
import com.example.demo.model.HouseTableBean;
import com.example.demo.model.UserTableBean;
import com.example.demo.service.CollectService;
import com.example.demo.service.HouseService;

@RestController
@RequestMapping("/api/houses")
@CrossOrigin(origins = "http://localhost:5174")
public class HouseController {
	@Autowired
	private HouseService houseService;

	@Autowired
	private CollectService collectService;

	@PostMapping("/add")
	@ResponseBody
	public Map<String, String> addHouse(
	        @RequestParam String title,
	        @RequestParam Integer price,
	        @RequestParam Integer size,
	        @RequestParam String address,
	        @RequestParam Byte room,
	        @RequestParam Byte bathroom,
	        @RequestParam Byte livingroom,
	        @RequestParam Byte kitchen,
	        @RequestParam Byte floor,
	        @RequestParam Boolean atticAddition,
	        @RequestParam Boolean washingMachine,
	        @RequestParam Boolean airConditioner,
	        @RequestParam Boolean network,
	        @RequestParam Boolean bedstead,
	        @RequestParam Boolean mattress,
	        @RequestParam Boolean refrigerator,
	        @RequestParam Boolean ewaterHeater,
	        @RequestParam Boolean gwaterHeater,
	        @RequestParam Boolean television,
	        @RequestParam Boolean channel4,
	        @RequestParam Boolean sofa,
	        @RequestParam Boolean tables,
	        @RequestParam Boolean pet,
	        @RequestParam Boolean parkingSpace,
	        @RequestParam Boolean elevator,
	        @RequestParam Boolean balcony,
	        @RequestParam Boolean shortTerm,
	        @RequestParam Boolean cooking,
	        @RequestParam Boolean waterDispenser,
	        @RequestParam Boolean managementFee,
	        @RequestParam Byte genderRestrictions,
	        @RequestParam List<MultipartFile> images) {

	    System.out.println("Received house details: " + title + ", images count: " + (images != null ? images.size() : 0));

	    // 建立 User 物件
	    UserTableBean user = new UserTableBean();
	    user.setUserId(1L);

	    // 建立 House 物件
	    HouseTableBean house = new HouseTableBean();
	    house.setUser(user);
	    house.setTitle(title);
	    house.setPrice(price);
	    house.setSize(size);
	    house.setAddress(address);
	    house.setRoom(room);
	    house.setBathroom(bathroom);
	    house.setLivingroom(livingroom);
	    house.setKitchen(kitchen);
	    house.setFloor(floor);
	    house.setAtticAddition(atticAddition);
	    house.setStatus((byte) 0);
	    house.setClickCount(0);

	    // 添加 Furniture 資訊
	    FurnitureTableBean furniture = new FurnitureTableBean();
	    furniture.setWashingMachine(washingMachine);
	    furniture.setAirConditioner(airConditioner);
	    furniture.setNetwork(network);
	    furniture.setBedstead(bedstead);
	    furniture.setMattress(mattress);
	    furniture.setRefrigerator(refrigerator);
	    furniture.setEwaterHeater(ewaterHeater);
	    furniture.setGwaterHeater(gwaterHeater);
	    furniture.setTelevision(television);
	    furniture.setChannel4(channel4);
	    furniture.setSofa(sofa);
	    furniture.setTables(tables);
	    furniture.setHouse(house);
	    house.setFurniture(furniture);

	    // 添加 Condition 資訊
	    ConditionTableBean condition = new ConditionTableBean();
	    condition.setPet(pet);
	    condition.setParkingSpace(parkingSpace);
	    condition.setElevator(elevator);
	    condition.setBalcony(balcony);
	    condition.setShortTerm(shortTerm);
	    condition.setCooking(cooking);
	    condition.setWaterDispenser(waterDispenser);
	    condition.setManagementFee(managementFee);
	    condition.setGenderRestrictions(genderRestrictions);
	    condition.setHouse(house);
	    house.setCondition(condition);

	    // 處理圖片
	    if (images != null && !images.isEmpty()) {
	        for (MultipartFile image : images) {
	            try {
	                byte[] imageBytes = image.getBytes();
	                HouseImageTableBean imageBean = new HouseImageTableBean();
	                imageBean.setImage(imageBytes); // 儲存 byte[] 圖片數據
	                imageBean.setHouse(house);
	                imageBean.setUser(user);
	                house.getImages().add(imageBean);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    } else {
	        System.out.println("No images received.");
	    }

	    // 保存房屋資訊
	    houseService.addHouse(house);

	    Map<String, String> response = Map.of("message", "房屋資訊已成功儲存");
	    return response;
	}


	

	@PutMapping("/{id}")
	public String updateHouse(@PathVariable Long id, @RequestBody HouseDetailsDTO detail) {
		return houseService.updateHouse(id, detail);
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

	@GetMapping("/user/{userId}")
	public List<HouseListByUserIdDTO> getHousesByUserId(@PathVariable Long userId) {
		return houseService.getHousesByUserId(userId);
	}

}
