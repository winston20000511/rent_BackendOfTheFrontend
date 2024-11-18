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
import com.example.demo.model.AdBean;
import com.example.demo.service.AdService;

@RestController
@RequestMapping("/advertisements")
public class AdRestController {

	private AdService adService;

	@Autowired
	public AdRestController(AdService adService) {
		this.adService = adService;
	}
	
	// get ads by user id and page
	@GetMapping("/{userId}/{pageNumber}")
	public List<AdBean> findAdsByUserIdAndPage(
			@PathVariable("userId") Integer userId, @PathVariable("pageNumber") Integer pageNumber) {
		return adService.findAdsByUserIdAndPage(userId, pageNumber);
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
}
