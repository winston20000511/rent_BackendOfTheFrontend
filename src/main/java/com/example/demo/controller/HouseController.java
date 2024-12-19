package com.example.demo.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.HouseDetailsDTO;
import com.example.demo.dto.HouseListByUserIdDTO;
import com.example.demo.dto.HouseOwnerInfoDTO;
import com.example.demo.helper.SearchHelper;
import com.example.demo.model.CollectTableBean;
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
	        @RequestParam String description,
	        @RequestParam String houseType,
	        @RequestParam("images") List<MultipartFile> images,
	        @RequestParam Date fromDate,
	        @RequestParam Date toDate,
	        @RequestParam Time fromTime,
	        @RequestParam Time toTime,
	        @RequestParam Short duration,
	        @RequestParam String weekDay
	    ) {

	        System.out.println("Received house details: " + title + ", images count: " + (images != null ? images.size() : 0));
	       	        
	        // 創建 User 物件
	        UserTableBean user = new UserTableBean();
	        user.setUserId(1L);

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
	        house.setStatus((byte) 1);
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

	        System.out.println("Received house details: " + title + ", images count: " + (images != null ? images.size() : 0));
	        
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
	        
	        //添加 HouseBookingTimeSlot 資訊
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
	

	

	@PutMapping("/{id}")
	public String updateHouse(@PathVariable Long id, @RequestBody HouseDetailsDTO detail) {
		return houseService.updateHouse(id, detail);
	}

	@GetMapping("/getPhotos/{houseId}")
	public ResponseEntity<?> getHousePhotos(@PathVariable Long houseId) {
	    List<byte[]> images = houseImageRepository.findImagesByHouseId(houseId);
	    if (images == null || images.isEmpty()) {
	        return ResponseEntity.ok(Collections.emptyList()); // 返回空數組
	    }

	    // 將每個圖片轉換為 Base64 編碼字串
	    List<String> base64Images = images.stream()
	            .map(image -> Base64.getEncoder().encodeToString(image))
	            .collect(Collectors.toList());

	    return ResponseEntity.ok(base64Images); // 返回 Base64 編碼的圖片數據
	}

	@GetMapping("/details/{houseId}")
	public ResponseEntity<HouseDetailsDTO> getHouseDetails(@PathVariable Long houseId) {
		HouseDetailsDTO houseDetails = houseService.getHouseDetails(houseId);
		return ResponseEntity.ok(houseDetails);
	}
	@GetMapping("/Description/{houseId}")
	public ResponseEntity<String> getHouseDescription(@PathVariable Long houseId) {
	    // 查找 HouseTable 資料
	    HouseTableBean house = houseRepository.findById(houseId)
	        .orElseThrow(() -> new RuntimeException("House not found"));

	    // 返回簡介部分
	    return ResponseEntity.ok(house.getDescription());  // 只返回簡介資料
	}

	
	@GetMapping("/Title/{houseId}")
	public ResponseEntity<String> getHouseTitle(@PathVariable Long houseId) {
	    // 查找 HouseTable 資料
	    HouseTableBean house = houseRepository.findById(houseId)
	        .orElseThrow(() -> new RuntimeException("House not found"));

	    // 返回簡介部分
	    return ResponseEntity.ok(house.getTitle());  // 只返回簡介資料
	}

	 @GetMapping("/Owner/{houseId}")
	    public ResponseEntity<HouseOwnerInfoDTO> getOwner(@PathVariable Long houseId) {
		 System.out.println(houseId);
	        try {
	            // 获取屋主信息
	            HouseOwnerInfoDTO houseOwnerInfo = userService.getOwnerInfo(houseId);
	            System.out.println("houseOwnerInfo:"+ houseOwnerInfo);
	            if (houseOwnerInfo == null) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	            }

	            return ResponseEntity.ok(houseOwnerInfo);
	        } catch (Exception e) {
	        	e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
	    }

	@DeleteMapping("/collect/delete/{userId}/{houseId}")
	public ResponseEntity<String> deleteCollectByUserIdAndHouseId(@PathVariable Long userId, @PathVariable Long houseId) {
	    try {
	        collectService.deleteByUserIdAndHouseId(userId, houseId);
	        return ResponseEntity.ok("Collect data deleted successfully for userId: " + userId + " and houseId: " + houseId);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Failed to delete collect data: " + e.getMessage());
	    }
	}
	@GetMapping("/collect/{userId}")
	public List<Long> getHouseIds(@PathVariable Long userId) {
		return collectService.getHouseIdsByUserId(userId);
	}
	 @PostMapping("/collect/add")
	    public ResponseEntity<CollectTableBean> addToCollection(@RequestBody CollectTableBean collect) {
	        // 設置收藏時間
	        collect.setCollectTime(LocalDateTime.now());
	        CollectTableBean collectedItem = collectService.addToCollection(collect);
	        return ResponseEntity.ok(collectedItem);
	    }

	@GetMapping("/user/{userId}")
	public List<HouseListByUserIdDTO> getHousesByUserId(@PathVariable Long userId) {
		return houseService.getHousesByUserId(userId);
	}

}
