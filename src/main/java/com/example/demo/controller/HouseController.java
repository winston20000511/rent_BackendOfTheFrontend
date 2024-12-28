package com.example.demo.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.HouseDetailsDTO;
import com.example.demo.dto.HouseListByUserIdDTO;
import com.example.demo.dto.HouseOwnerInfoDTO;
import com.example.demo.helper.JwtUtil;
import com.example.demo.helper.SearchHelper;
import com.example.demo.helper.UnTokenException;
import com.example.demo.model.ConditionTableBean;
import com.example.demo.model.FurnitureTableBean;
import com.example.demo.model.HouseBookingTimeSlotBean;
import com.example.demo.model.HouseImageTableBean;
import com.example.demo.model.HouseTableBean;
import com.example.demo.model.UserTableBean;
import com.example.demo.repository.HouseImageRepository;
import com.example.demo.repository.HouseRepository;
import com.example.demo.service.CollectService;
import com.example.demo.service.HouseService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/houses")
@CrossOrigin(origins = "http://localhost:5173")
public class HouseController {
	@Autowired
	private HouseImageRepository houseImageRepository;
	@Autowired
	private HouseRepository houseRepository;
	@Autowired
	private HouseService houseService;

	@Autowired
	private CollectService collectService;

	@Autowired
	private UserService userService;

	@Autowired
	private SearchHelper searchHelper;

	@PostMapping("/add")
	@ResponseBody
	public Map<String, String> addHouse(@RequestHeader("authorization") String authorizationHeader,
			@RequestParam String title, @RequestParam Integer price, @RequestParam Integer size,
			@RequestParam String address, @RequestParam Byte room, @RequestParam Byte bathroom,
			@RequestParam Byte livingroom, @RequestParam Byte kitchen, @RequestParam Byte floor,
			@RequestParam Boolean atticAddition, @RequestParam Boolean washingMachine,
			@RequestParam Boolean airConditioner, @RequestParam Boolean network, @RequestParam Boolean bedstead,
			@RequestParam Boolean mattress, @RequestParam Boolean refrigerator, @RequestParam Boolean ewaterHeater,
			@RequestParam Boolean gwaterHeater, @RequestParam Boolean television, @RequestParam Boolean channel4,
			@RequestParam Boolean sofa, @RequestParam Boolean tables, @RequestParam Boolean pet,
			@RequestParam Boolean parkingSpace, @RequestParam Boolean elevator, @RequestParam Boolean balcony,
			@RequestParam Boolean shortTerm, @RequestParam Boolean cooking, @RequestParam Boolean waterDispenser,
			@RequestParam Boolean managementFee, @RequestParam Byte genderRestrictions,
			@RequestParam String description, @RequestParam String houseType,
			@RequestParam("images") List<MultipartFile> images,
			@RequestParam LocalDate fromDate,
	        @RequestParam LocalDate toDate,
	        @RequestParam LocalTime fromTime,
	        @RequestParam LocalTime toTime,
	        @RequestParam Short duration,
	        @RequestParam String weekDay) {

		System.out.println(
				"Received house details: " + title + ", images count: " + (images != null ? images.size() : 0));

		// 創建 User 物件
		UserTableBean user = new UserTableBean();
		Long userId = extractUserIdFromToken(authorizationHeader);
		user.setUserId(userId);

		// 創建 House 物件
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
		house.setDescription(description);
		house.setHouseType(houseType);
		house.setStatus((byte) 0);
		house.setClickCount(0);

		try {
			JSONObject geocodeJson = searchHelper.fetchGeocodeFromAPI(address); // 使用 searchHelper 調用方法
			Optional<double[]> latLng = searchHelper.parseLatLng(geocodeJson);

			if (latLng.isPresent()) {
				house.setLat(latLng.get()[0]); // 設置緯度
				house.setLng(latLng.get()[1]); // 設置經度
			} else {
				System.out.println("無法獲取經緯度，將 lat 和 lng 設置為 null");
				house.setLat(null);
				house.setLng(null);
			}
		} catch (IOException e) {
			e.printStackTrace();
			house.setLat(null);
			house.setLng(null);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

		System.out.println(
				"Received house details: " + title + ", images count: " + (images != null ? images.size() : 0));

		// 處理圖片
		if (images != null && !images.isEmpty()) {
			for (MultipartFile image : images) {
				try {
					byte[] imageBytes = image.getBytes(); // 獲取圖片的 byte[]
					HouseImageTableBean imageBean = new HouseImageTableBean();
					imageBean.setImages(imageBytes); // 儲存 byte[] 圖片數據
					imageBean.setHouse(house);
					imageBean.setUser(user);
					house.getImages().add(imageBean); // 添加到房屋的圖片列表中
					System.out.println("ok");
				} catch (Exception e) {
					System.err.println("Error processing image: " + e.getMessage());
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("No images received.");
		}

		// 添加 HouseBookingTimeSlot 資訊
		HouseBookingTimeSlotBean booking = new HouseBookingTimeSlotBean();
		booking.setFromDate(fromDate);
		booking.setToDate(toDate);
		booking.setFromTime(fromTime);
		booking.setToTime(toTime);
		booking.setDuration(duration);
		booking.setWeekDay(weekDay);
		booking.setHouse(house);
		house.setBookingTimeSlots(booking);

		// 保存房屋資訊
		houseService.addHouse(house);

		Map<String, String> response = Map.of("message", "房屋資訊已成功儲存");
		return response;
	}

	 @PostMapping("/update/{houseId}")
	    public ResponseEntity<?> updateHouse(@RequestHeader("authorization") String authorizationHeader,
	            @PathVariable Long houseId,
	            @RequestParam("houseData") String houseDataJson, 
	            @RequestParam("furnitureServices") String furnitureServicesJson,
	            @RequestParam("houseRestrictions") String houseRestrictionsJson,
	            @RequestParam(value = "existingImageIds", required = false) List<Long> existingImageIds,
	            @RequestParam(value = "newImages", required = false) List<MultipartFile> newImages
	    ) {
	        try {
	        	Long userId = extractUserIdFromToken(authorizationHeader);
	            // 將前端傳來的 JSON字串 反序列化成 Map/物件
	            ObjectMapper mapper = new ObjectMapper();
	            Map<String, String> houseData = mapper.readValue(houseDataJson, new TypeReference<Map<String, String>>() {});
	            Map<String, Boolean> furnitureServices = mapper.readValue(furnitureServicesJson, new TypeReference<Map<String, Boolean>>() {});
	            Map<String, Boolean> houseRestrictions = mapper.readValue(houseRestrictionsJson, new TypeReference<Map<String, Boolean>>() {});

	            // 如果前端沒帶 existingImageIds，就給個空的 List
	            if (existingImageIds == null) {
	                existingImageIds = new ArrayList<>();
	            }

	            // 調用 Service
	            houseService.updateHouse(houseId, houseData, furnitureServices, houseRestrictions, newImages, existingImageIds,userId);
	            return ResponseEntity.ok("房屋更新成功！");
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新失敗：" + e.getMessage());
	        }
	    }

	 @GetMapping("/getPhotos/{houseId}")
	 public ResponseEntity<?> getHousePhotos(@PathVariable Long houseId) {
	     List<HouseImageTableBean> images = houseImageRepository.findByHouseId(houseId);
	     if (images.isEmpty()) {
	         return ResponseEntity.ok(Collections.emptyList());
	     }

	     // 將每張圖片轉成 { "id": 123, "base64": "xxxx" }
	     List<Map<String, String>> result = images.stream()
	         .map(img -> {
	             Map<String, String> map = new HashMap<>();
	             map.put("id", String.valueOf(img.getImageId()));
	             String base64 = Base64.getEncoder().encodeToString(img.getImages());
	             map.put("base64", base64);
	             return map;
	         })
	         .collect(Collectors.toList());

	     return ResponseEntity.ok(result);
	 }

	@GetMapping("/details/{houseId}")
	public ResponseEntity<HouseDetailsDTO> getHouseDetails(@PathVariable Long houseId) {
		HouseDetailsDTO houseDetails = houseService.getHouseDetails(houseId);
		return ResponseEntity.ok(houseDetails);
	}

	@GetMapping("/Description/{houseId}")
	public ResponseEntity<Map<String, String>> getHouseDescription(@PathVariable Long houseId) {
		// 查找 HouseTable 資料
		HouseTableBean house = houseRepository.findById(houseId)
				.orElseThrow(() -> new RuntimeException("House not found"));

		// 创建一个 Map 来包装返回的 JSON 数据
		Map<String, String> response = new HashMap<>();
		response.put("description", house.getDescription()); // 将描述放入 Map

		// 返回 JSON 格式的响应
		return ResponseEntity.ok(response);
	}

	@GetMapping("/Title/{houseId}")
	public ResponseEntity<String> getHouseTitle(@PathVariable Long houseId) {
		// 查找 HouseTable 資料
		HouseTableBean house = houseRepository.findById(houseId)
				.orElseThrow(() -> new RuntimeException("House not found"));

		// 返回簡介部分
		return ResponseEntity.ok(house.getTitle()); // 只返回簡介資料
	}

	@GetMapping("/ownerInfo/{houseId}")
    public ResponseEntity<HouseOwnerInfoDTO> getOwnerInfo(@PathVariable Long houseId) {
        HouseOwnerInfoDTO ownerInfo = houseService.getOwnerInfoByHouseId(houseId);
        if (ownerInfo != null) {
            return ResponseEntity.ok(ownerInfo);
        }
        return ResponseEntity.notFound().build();
    }

//	 COLLECT FUNCTION
	@DeleteMapping("collect/delete/{houseId}")
	public ResponseEntity<String> deleteCollect(@RequestHeader("authorization") String authorizationHeader,
			@PathVariable Long houseId) {
		try {
			Long userId = extractUserIdFromToken(authorizationHeader);

			// 執行刪除邏輯
			collectService.deleteByUserIdAndHouseId(userId, houseId);

			return ResponseEntity.ok("成功刪除收藏資料，userId: " + userId + " houseId: " + houseId);
		} catch (UnTokenException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("刪除失敗，發生未知錯誤");
		}
	}

	@GetMapping("/collect")
	public ResponseEntity<?> getHouseIds(@RequestHeader("authorization") String authorizationHeader) {
	    try {
	        Long userId = extractUserIdFromToken(authorizationHeader);
	        List<Long> houseIds = collectService.getHouseIdsByUserId(userId);
	        if (houseIds == null) {
	            houseIds = Collections.emptyList();
	        }
	        return ResponseEntity.ok(houseIds);
	    } catch (UnTokenException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	    } catch (Exception e) {
	        e.printStackTrace(); // 打印完整的堆棧日誌
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("獲取房屋 ID 失敗，發生未知錯誤");
	    }
	}
  

	@GetMapping("/houses")
	public ResponseEntity<?> getHouses(@RequestHeader("authorization") String authorizationHeader) {
		try {
			// 從 Token 解析出 email
			Long userId = extractUserIdFromToken(authorizationHeader);

			// 調用服務層邏輯，獲取房屋列表
			List<HouseListByUserIdDTO> houses = houseService.getHousesByUserId(userId);

			return ResponseEntity.ok(houses);
		} catch (UnTokenException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("獲取房屋列表失敗，發生未知錯誤");
		}
	}

	@GetMapping("/collect/exists/{houseId}")
	public boolean isFavorited(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long houseId) {
		Long userId = extractUserIdFromToken(authorizationHeader);
		return collectService.isHouseFavorited(userId, houseId);
	}

	// 新增收藏
	@PostMapping("/collect/add/{houseId}")
	public ResponseEntity<String> addCollect(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long houseId) {
	    Long userId = extractUserIdFromToken(authorizationHeader);
	    collectService.addFavorite(userId, houseId);
	    
	    // 返回 OK
	    return ResponseEntity.ok("OK");
	}

	// 移除收藏
	@DeleteMapping("/collect/remove/{houseId}")
	public ResponseEntity<String> removeFavorite(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long houseId) {
		Long userId = extractUserIdFromToken(authorizationHeader);
		collectService.deleteByUserIdAndHouseId(userId, houseId);
		
		return ResponseEntity.ok("OK");
	}

//	    將TOKEN中的USERID取出
	private Long extractUserIdFromToken(String authorizationHeader) {
		String[] userInfo = JwtUtil.verify(authorizationHeader);
		return Long.parseLong(userInfo[1]);
	}

	@PutMapping("/{houseId}/status")
	public ResponseEntity<String> updateHouseStatus(@PathVariable Long houseId) {
		boolean isUpdated = houseService.updateHouseStatusToTwo(houseId);
		if (isUpdated) {
			return ResponseEntity.ok("房屋狀態已從0或1更新為2。");
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	@PutMapping("/{houseId}/incrementClick")
    public ResponseEntity<String> incrementClickCount(@PathVariable Long houseId) {
        try {
            houseService.incrementClickCount(houseId);
            return ResponseEntity.ok("Click count incremented successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to increment click count");
        }
    }
	
	 @GetMapping("/{houseId}/clickCount")
	    public ResponseEntity<Integer> getClickCount(@PathVariable Long houseId) {
	        try {
	            Integer clickCount = houseService.getClickCount(houseId);
	            return ResponseEntity.ok(clickCount);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
	    }
}
