package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AdCreationRequestDTO;
import com.example.demo.dto.AdDetailsResponseDTO;
import com.example.demo.model.AdBean;
import com.example.demo.model.AdtypeBean;
import com.example.demo.service.AdService;
import com.example.demo.service.HouseService;

@RestController
@RequestMapping("/advertisements")
public class AdRestController {

	private AdService adService;
	private HouseService houseService;

	@Autowired
	public AdRestController(AdService adService, HouseService houseService) {
		this.adService = adService;
		this.houseService = houseService;
	}
	

	@GetMapping("/noadhouses/{userId}")
	public List<Map<String,Object>> findNoAdHousesByUserId(@PathVariable("userId") Long userId){
		return houseService.findNoAdHousesByUserId(userId);
	}
	
	
	@GetMapping("/adtype")
	public List<AdtypeBean> findAdType() {
		return adService.findAllAdType();
	}
	
	
	@PostMapping
	public boolean createAds(@RequestBody List<AdCreationRequestDTO> adCreationRequestDTOs) {
		return adService.createAds(adCreationRequestDTOs);
	}
	
	@PutMapping("/{adId}/{newAdtypeId}")
	public ResponseEntity<?> updateAdtypeById(@PathVariable("adId") Long adId, @PathVariable("newAdtypeId") Integer newAdtypeId) {
		
		// 會員登入驗證
		
		System.out.println(newAdtypeId);
		AdBean updatedAd = adService.updateAdtypeById(adId, newAdtypeId);
		if(updatedAd == null) {
			// 之後製作禁止修改訂單的錯誤訊息頁面 + 前端網頁控管
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("無法修改");
		}
		
		return ResponseEntity.ok(updatedAd);
		
	}
	
	// delete an ad by ad id
	@DeleteMapping("/{adId}")
	public boolean deleteAdById(@PathVariable("adId") Long adId) {
		return adService.deleteAdById(adId);
	}
	
	
	
	/* DTO: get ad details */
	@GetMapping("/details/{adId}")
	public AdDetailsResponseDTO findAdsByAdId(@PathVariable("adId") Long adId) {
		AdDetailsResponseDTO adDetails = adService.findAdDetailsByAdId(adId);
		System.out.println(adDetails.toString());
		return adDetails;
	}
	
	
	@PostMapping("/search")
	public List<AdDetailsResponseDTO> findAdTableDataByAdIdAndIsPaid(
			@RequestBody Map<String, Object> filter //, HttpSession session
			){
		
		//		Long loginUserId = (Long)session.getAttribute("loginUserId");

		Long userId = Long.valueOf(((Integer)filter.get("userId")).longValue());
		Boolean isPaid = (Boolean)filter.get("isPaid");
		Integer pageNumber = (Integer)filter.get("pageNumber")==null? 1 : (Integer)filter.get("pageNumber");
		System.out.println(filter);
		
		List<AdDetailsResponseDTO> dtos = adService.findAdTableDataByUserIdAndIsPaid(userId, isPaid, pageNumber);
		
		return dtos;
	}
	
}
