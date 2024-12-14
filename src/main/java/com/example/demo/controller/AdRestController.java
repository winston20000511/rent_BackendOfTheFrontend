package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.example.demo.model.AdtypeBean;
import com.example.demo.service.AdService;

@RestController
@RequestMapping("/advertisements")
public class AdRestController {

	private AdService adService;

	@Autowired
	public AdRestController(AdService adService) {
		this.adService = adService;
	}
	
	// 取得所有廣告：用戶id
	@GetMapping("/{pageNumber}")
	public Page<AdDetailsResponseDTO> findAllAds(@PathVariable Integer pageNumber) {
		return adService.findAllAds(pageNumber);
	}

	// 依條件篩選：用戶id + 付款狀態 + 上架時間
	@PostMapping("/filter")
	public Page<AdDetailsResponseDTO> filter(@RequestBody Map<String, String> conditions) {
		// {page=1, daterange=week, paymentstatus=paid}
		Page<AdDetailsResponseDTO> pages= adService.findAdsByConditions(conditions);
		
		return pages;
	}
	
	// 找到沒有上廣告的房子：用戶id篩選
	@GetMapping("/houseswithoutadds/{pageNumber}")
	public Page<Map<String, Object>> filterHousesWithoutAds(@PathVariable("pageNumber") Integer pageNumber){
		
		Page<Map<String, Object>> housesWithoutAds = adService.findHousesWithoutAds(pageNumber);
		return housesWithoutAds;
	}
	
	// 取得廣告種類
	@GetMapping("/adtypes")
	public List<AdtypeBean> findAllAdtypes(){
		return adService.findAllAdType();
	}
	
	// 取得廣告詳細資料
	@GetMapping("/adId/{adId}")
	public AdDetailsResponseDTO findAdDetails(@PathVariable Long adId) {
		return adService.findAdDetailsByAdId(adId);
	}
	
	// 新增
	@PostMapping
	public boolean createAd(@RequestBody AdCreationRequestDTO requestDTO) {
		return adService.createAd(requestDTO);
	}
	
	// 刪除
	@DeleteMapping
	public boolean deleteAd(@RequestBody Long adId) {
		return adService.deleteAdById(adId);
	}
	
	// 修改
	@PutMapping
	public AdDetailsResponseDTO update(@RequestBody Map<String, String> request) {
		
		Long adId = Long.parseLong(request.get("adId"));
		Integer adtypeId = Integer.parseInt(request.get("newAdtypeId"));
		
		return adService.updateAdtypeById(adId, adtypeId);
	}
}
