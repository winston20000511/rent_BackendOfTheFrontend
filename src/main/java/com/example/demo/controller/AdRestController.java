package com.example.demo.controller;

import java.util.List;

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
import com.example.demo.service.AdService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/advertisements")
public class AdRestController {

	private AdService adService;

	@Autowired
	public AdRestController(AdService adService) {
		this.adService = adService;
	}
	
	// get ads by user id and is paid and page
	@GetMapping("/{userId}/{isPaid}/{pageNumber}")
	public List<AdBean> findAdsBydAndIsPaidAndPage(
			@PathVariable("userId") Long userId,
			//HttpSession session,
			@PathVariable("isPaid") Boolean isPaid,
			@PathVariable("pageNumber") Integer pageNumber) {
//		Long loginUserId = (Long)session.getAttribute("loginUserId");
//		loginUserId = (long) 5; //先測試
		return adService.findAdsByUserIdAndIsPaidAndPage(userId, isPaid, pageNumber);
	}
	
	// create a new ad
	@PostMapping
	public AdBean ceateAd(@RequestBody AdCreationRequestDTO adCreationRequestDTO) {
		return adService.createAd(adCreationRequestDTO);
	}
	
	// update an ad by ad id
	@PutMapping("/{adId}/{newAdtypeId}")
	public ResponseEntity<?> updateAdtypeById(@PathVariable("adId") Long adId, @PathVariable("newAdtypeId") Integer newAdtypeId) {
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
	// get an ad by ad id
	@GetMapping("/details/{adId}")
	public AdDetailsResponseDTO findAdsByAdId(@PathVariable("adId") Long adId) {
		AdDetailsResponseDTO adDetails = adService.findAdDetailsByAdId(adId);
		System.out.println(adDetails.toString());
		return adDetails;
	}
	
	@GetMapping("/tabledata/{userId}/{isPaid}/{pageNumber}")
	public List<AdDetailsResponseDTO> findAdTableDataByAdIdAndIsPaid(
			@PathVariable("userId") Long userId,
			@PathVariable("isPaid") Boolean isPaid,
			@PathVariable("pageNumber") Integer pageNumber){
		List<AdDetailsResponseDTO> dtos = adService.findAdTableDataByUserIdAndIsPaid(userId, isPaid, pageNumber);
		return dtos;
	}
	
}
