package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AdCreationRequestDTO;
import com.example.demo.dto.AdDetailsResponseDTO;
import com.example.demo.helper.JwtUtil;
import com.example.demo.model.AdtypeBean;
import com.example.demo.service.AdService;

@RestController
@RequestMapping("/api/advertisements")
public class AdRestController {

	private Logger logger = Logger.getLogger(AdRestController.class.getName());
	
	private AdService adService;

	public AdRestController(AdService adService) {
		this.adService = adService;
	}
	
	@PostMapping("/adtypes")
	public List<AdtypeBean> findAllAdtypes(@RequestHeader("authorization") String authorizationHeader){
		JwtUtil.verify(authorizationHeader);
		return adService.findAllAdType();
	}

	/**
	 * 接收使用者端傳來的篩選條件 conditions:
	 * String page
	 * String paymentstatus(all/paid/unpaid)
	 * String daterange(all/week/month/year)
	 * String input
	 * 
	 * @param conditions
	 * @param authorizationHeader
	 * @return
	 */
	@PostMapping("/filter")
	public Page<AdDetailsResponseDTO> filter(
			@RequestBody Map<String, String> conditions, @RequestHeader("authorization") String authorizationHeader) {
		
		logger.info("廣告篩選條件: " + conditions);
		
		String[] userInfo = JwtUtil.verify(authorizationHeader);
		Long userId = Long.parseLong(userInfo[1]);
		
		Integer pageNumber = Integer.parseInt(conditions.get("page"));
		String dateRange = conditions.get("daterange");
        String paymentStatus = conditions.get("paymentstatus");
        String houseTitle = conditions.get("housetitle");
        
		Page<AdDetailsResponseDTO> pages= adService.findAdsByConditions(
				userId, pageNumber, dateRange, paymentStatus, houseTitle);

		logger.info("ad filter: " + pages.toString());
		return pages;
	}
	
	@PostMapping("/houses/withoutads")
	public Page<Map<String, Object>> filterHousesWithoutAds(
			@RequestBody Integer pageNumber, @RequestHeader("authorization") String authorizationHeader){

		String[] userInfo = JwtUtil.verify(authorizationHeader);
		Long userId = Long.parseLong(userInfo[1]);
		
		Page<Map<String, Object>> housesWithoutAds = adService.findHousesWithoutAds(userId, pageNumber);
		
		return housesWithoutAds;
	}
	
	
	@PostMapping("/adId")
	public AdDetailsResponseDTO findAdDetails(
			@RequestBody Long adId, @RequestHeader("authorization") String authorizationHeader) {

		String[] userInfo = JwtUtil.verify(authorizationHeader);
		Long userId = Long.parseLong(userInfo[1]);
		
		return adService.findAdDetailsByAdId(userId, adId);
	}
	
	
	@PostMapping
	public boolean createAd(
	        @RequestBody AdCreationRequestDTO requestDTO, @RequestHeader("authorization") String authorizationHeader) {
	    
	    String[] userInfo = JwtUtil.verify(authorizationHeader);
	    Long userId = Long.parseLong(userInfo[1]);
	    
	    return adService.createAd(userId, requestDTO);
	    
	}

	@Transactional
	@DeleteMapping
	public boolean deleteAdById(
			@RequestBody Long adId, @RequestHeader("authorization") String authorizationHeader) {
		
		String[] userInfo = JwtUtil.verify(authorizationHeader);
		Long userId = Long.parseLong(userInfo[1]);
		
		return adService.deleteAdById(userId, adId);
	}
	
	
	/**
	 * 更新未付款的推播服務之種類
	 * @param request
	 * @param authorizationHeader
	 * @return
	 */
	@Transactional
	@PutMapping
	public AdDetailsResponseDTO updateAdById(
			@RequestBody Map<String, String> request, @RequestHeader("authorization") String authorizationHeader) {
		
		String[] userInfo = JwtUtil.verify(authorizationHeader);
		Long userId = Long.parseLong(userInfo[1]);
		
		Long adId = Long.parseLong(request.get("adId"));
		Integer adtypeId = Integer.parseInt(request.get("newAdtypeId"));
		
		return adService.updateAdtypeByAdId(userId, adId, adtypeId);
	}

}
