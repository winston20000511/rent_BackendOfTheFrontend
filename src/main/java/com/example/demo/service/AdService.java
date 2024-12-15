package com.example.demo.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AdCreationRequestDTO;
import com.example.demo.dto.AdDetailsResponseDTO;
import com.example.demo.model.AdBean;
import com.example.demo.model.AdtypeBean;
import com.example.demo.repository.AdRepository;
import com.example.demo.repository.AdSpecification;
import com.example.demo.repository.AdtypeRepository;
import com.example.demo.repository.HouseRepository;



@Service
public class AdService {
	
	@Autowired
	private AdRepository adRepository;
	@Autowired
	private AdtypeRepository adtypeRepository;
	@Autowired
	private HouseRepository houseRepository;
	
	public AdService(AdRepository adRepository, AdtypeRepository adtypeRepository, HouseRepository houseRepository) {
		this.adRepository = adRepository;
		this.adtypeRepository = adtypeRepository;
		this.houseRepository = houseRepository;
	}
	

	/* AdBean */
	public boolean createAd(AdCreationRequestDTO requestDTO) {
		
			AdBean adBean = new AdBean();
			adBean.setUserId(requestDTO.getUserId());
			adBean.setHouseId(requestDTO.getHouseId());
			
			Optional<AdtypeBean> optional = adtypeRepository.findById(requestDTO.getAdtypeId());
			if(optional != null) {
				AdtypeBean adtypeBean = optional.get();
				adBean.setAdtypeId(adtypeBean.getAdtypeId());
				adBean.setAdPrice(adtypeBean.getAdPrice());
			}
			
			adBean.setIsPaid(false);
			
			adRepository.save(adBean);
		
		return true;
	}
	

	public AdDetailsResponseDTO updateAdtypeById(Long adId, Integer newAdtypeId) {
		Optional<AdBean> optional = adRepository.findById(adId);
		AdDetailsResponseDTO responseDTO = null;
		if(optional != null) {
			AdBean ad = optional.get();
			ad.setAdtypeId(newAdtypeId);
			ad.setAdtype(adtypeRepository.findById(newAdtypeId).get());
			Integer price = adtypeRepository.findById(newAdtypeId).get().getAdPrice();
			ad.setAdPrice(price);
			AdBean savedAd = adRepository.save(ad);
			
			responseDTO = setAdDetailResponseDTO(savedAd);
		}
		
		return responseDTO;
	}
	

	public boolean deleteAdById(Long adId) {
		Optional<AdBean> optional = adRepository.findById(adId);
		if(optional.isPresent()) {
			adRepository.deleteById(adId);
			return true;
		}
		
		return false;
	}
	
	
	/* AdtypeBean */
	public List<AdtypeBean> findAllAdType(){
		return adtypeRepository.findAll();
	}
	
	
	/* DTOs */	
	// 之後要加 userId
	public Page<AdDetailsResponseDTO> findAllAds(Integer pageNumber){
		Pageable pageable = PageRequest.of(pageNumber-1, 10, Sort.Direction.DESC, "adId");
		Page<AdBean> adPages = adRepository.findAll(pageable);
		List<AdBean> ads = adPages.getContent();
		List<AdDetailsResponseDTO> responseDTOs = setAdDetailsResponseDTO(ads);
		
		return new PageImpl<>(responseDTOs, pageable, adPages.getTotalElements());
	}
	
	public AdDetailsResponseDTO findAdDetailsByAdId(Long adId){
		AdDetailsResponseDTO responseDTO = null;
		Optional<AdBean> optional = adRepository.findById(adId);
		
		if(optional.isPresent()) {
			AdBean ad = optional.get();
			responseDTO = setAdDetailResponseDTO(ad);
		}
		
		return responseDTO;
	}
	
	public Page<AdDetailsResponseDTO> findAdsByConditions(Map<String, String> conditions) {
		Integer pageNumber = Integer.parseInt(conditions.get("page"));
		
		// 建立篩選條件
		Specification<AdBean> spec = AdSpecification.filter(conditions);
		
		Pageable pageable = PageRequest.of(pageNumber-1, 10, Sort.Direction.DESC, "adId");
		Page<AdBean> page = adRepository.findAll(spec, pageable);
		
		List<AdDetailsResponseDTO> responseDTOs = setAdDetailsResponseDTO(page.getContent());
		
		return new PageImpl<>(responseDTOs, pageable, page.getTotalElements());
	}
	
	/* 搜尋用戶沒有上廣告的房屋 */
	public Page<Map<String, Object>> findHousesWithoutAds(Integer pageNumber){
		Pageable pageable = PageRequest.of(pageNumber-1, 10, Sort.Direction.DESC, "houseId");
		// info: houseId + houseTitle
		Page<Map<String, Object>> housesInfo = houseRepository.findHousesWithoutAds(pageable);
		return housesInfo;
	}
	
	
	/* private methods */
	private List<AdDetailsResponseDTO> setAdDetailsResponseDTO(List<AdBean> ads) {
		List<AdDetailsResponseDTO> responseDTOs = new ArrayList<>();
		for(AdBean ad : ads) {
			AdDetailsResponseDTO responseDTO = new AdDetailsResponseDTO();
			responseDTO.setAdId(ad.getAdId());
			responseDTO.setUserId(ad.getUserId());
			responseDTO.setHouseTitle(ad.getHouse().getTitle());
			responseDTO.setAdName(ad.getAdtype().getAdName());
			responseDTO.setAdPrice(ad.getAdPrice());
			responseDTO.setIsPaid(ad.getIsPaid());
			responseDTO.setOrderId(ad.getOrderId());
			responseDTO.setPaidDate(ad.getPaidDate());
			
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime paidDate = ad.getPaidDate();
			if(ad.getPaidDate() != null) {
				Duration duration = Duration.between(now, paidDate);
				responseDTO.setRemainingDays(duration.toDays());
			}
	
			responseDTOs.add(responseDTO);
			System.out.println(responseDTO.toString());
		}
		
		return responseDTOs;
	}
	
	private AdDetailsResponseDTO setAdDetailResponseDTO(AdBean ad) {
		AdDetailsResponseDTO responseDTO = new AdDetailsResponseDTO();
		responseDTO.setAdId(ad.getAdId());
		responseDTO.setUserId(ad.getUserId());
		responseDTO.setHouseTitle(ad.getHouse().getTitle());
		responseDTO.setAdName(ad.getAdtype().getAdName());
		responseDTO.setAdPrice(ad.getAdPrice());
		responseDTO.setIsPaid(ad.getIsPaid());
		responseDTO.setOrderId(ad.getOrderId());
		responseDTO.setPaidDate(ad.getPaidDate());
		
		// 計算廣到剩餘時間
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime paidDate = ad.getPaidDate();
		if(ad.getPaidDate() != null) {
			Duration duration = Duration.between(now, paidDate);
			responseDTO.setRemainingDays(duration.toDays());
			responseDTO.setRemainingDays(duration.toDays());
		}
		
		return responseDTO;
	}
	
}
