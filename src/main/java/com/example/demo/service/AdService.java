package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AdCreationRequestDTO;
import com.example.demo.dto.AdDetailsResponseDTO;
import com.example.demo.model.AdBean;
import com.example.demo.model.AdtypeBean;
import com.example.demo.repository.AdRepository;
import com.example.demo.repository.AdtypeRepository;

import jakarta.persistence.Tuple;

@Service
public class AdService {
	
	private AdRepository adRepository;
	private AdtypeRepository adtypeRepository;
	
	@Autowired
	public AdService(AdRepository adRepository, AdtypeRepository adtypeRepository) {
		this.adRepository = adRepository;
		this.adtypeRepository = adtypeRepository;
	}
	
	/* CRUD */
	
	// get ads by user id and page
//	public List<AdBean> findAdsByUserIdAndIsPaidAndPage(Long userId, Boolean isPaid, Integer pageNumber){
//		Pageable pageable = PageRequest.of(pageNumber-1, 10, Sort.Direction.DESC, "adId");
//		List<AdBean> ads = adRepository.findAdsByUserIdAndIsPaidAndPage(userId, isPaid, pageable);
//		return ads;
//	}
	
	// create a new ad
	public AdBean createAd(AdCreationRequestDTO adCreationRequestDTO) {
		AdBean adBean = new AdBean();
		adBean.setUserId(adCreationRequestDTO.getUserId());
		adBean.setHouseId(adCreationRequestDTO.getHouseId());
		Integer adtypeId = adCreationRequestDTO.getAdtypeId();
		adBean.setAdtypeId(adtypeId);
		adBean.setIsPaid(false);
		
		Optional<AdtypeBean> optional = adtypeRepository.findById(adtypeId);
		if(optional.isEmpty()) return null;
		
		AdtypeBean adtype = optional.get();
		
		adBean.setAdPrice(adtype.getAdPrice());
		
		return adRepository.save(adBean);
	}
	
	public List<AdDetailsResponseDTO> findAdsByUserIdAndIsPaidAndPage(Long userId, Boolean isPaid, Integer pageNumber){
		Pageable pageable = PageRequest.of(pageNumber-1, 10, Sort.Direction.DESC, "adId");
		List<AdBean> ads = adRepository.findAdsByUserIdAndIsPaidAndPage(userId, isPaid, pageable);
		
		List<AdDetailsResponseDTO> detailList = new ArrayList<>();
		for(AdBean ad : ads) {
			AdDetailsResponseDTO detail = new AdDetailsResponseDTO();
			detail.setAdId(ad.getAdId());
			detail.setHouseTitle(ad.getHouse().getTitle());
			detail.setAdPrice(ad.getAdPrice());
			detailList.add(detail);
		}
		
		return detailList;
	}
	
	// get all ad types
	public List<AdtypeBean> findAllAdType(){
		return adtypeRepository.findAll();
	}
	
	// create a new ad
	public boolean createAds(List<AdCreationRequestDTO> adCreationRequestDTOs) {
		
		for(AdCreationRequestDTO dto : adCreationRequestDTOs) {
			AdBean adBean = new AdBean();
			adBean.setIsPaid(false);
			adBean.setUserId(dto.getUserId());
			adBean.setHouseId(dto.getHouseId());
			Optional<AdtypeBean> adtypes = adtypeRepository.findById(dto.getAdtypeId());
			
			adBean.setAdtypeId(dto.getAdtypeId());
			
			adBean.setAdPrice(adtypes.get().getAdPrice());
			
//			Map<String, Integer> adtypeMap = new HashMap<>();
//			for(Tuple tuple : results) {
//				Integer adPrice = tuple.get(1, Integer.class);
//				
//				adtypeMap.put("adtypeId", dto.getAdtypeId());
//				adtypeMap.put("adPrice", adPrice);
//			}
//			
//			adBean.setAdtypeId(adtypeMap.get("adtypeId"));
//			adBean.setAdPrice(adtypeMap.get("adPrice"));
			
			adRepository.save(adBean);
		}
		
		return true;
	}
	
	// udpate the adtype of an ad by ad id
	public AdBean updateAdtypeById(Long adId, Integer newAdtypeId) {
		
		Optional<AdBean> adOptional = adRepository.findById(adId);
		if(adOptional.isPresent()) {
			AdBean ad = adOptional.get();
			if(ad.getIsPaid()) return null;
			
			ad.setAdtypeId(newAdtypeId);
			
			Optional<AdtypeBean> adTypeOptional = adtypeRepository.findById(newAdtypeId);
			
			if(adTypeOptional.isPresent()) {
				AdtypeBean adType = adTypeOptional.get();
				ad.setAdPrice(adType.getAdPrice());
				adRepository.save(ad);
				
				return ad;
			}
		}
		return null;
	}
	
	// delete an order by id
	public boolean deleteAdById(Long adId) {
		Optional<AdBean> optional = adRepository.findById(adId);
		if(optional.isPresent()) {
			adRepository.deleteById(adId);
			return true;
		}
		
		return false;
	}
	
	
	/* DTOs */	
	public AdDetailsResponseDTO findAdDetailsByAdId(Long adId){
		AdDetailsResponseDTO adDetails = adRepository.findAdDetailsByAdId(adId);
		return adDetails;
	}
	
	public List<AdDetailsResponseDTO> findAdTableDataByUserIdAndIsPaid(Long userId, Boolean isPaid, Integer pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber-1, 10, Sort.Direction.DESC, "adId");
		List<AdBean> adsData = adRepository.findAdsByUserIdAndIsPaidAndPage(userId, isPaid, pageable);
		
		List<AdDetailsResponseDTO> adTableDataList = new ArrayList<>();
		for(AdBean adData : adsData) {
			AdDetailsResponseDTO adTableDataDTO = new AdDetailsResponseDTO();
			adTableDataDTO.setAdId(adData.getAdId());
			adTableDataDTO.setAdName(adData.getAdtype().getAdName());
			adTableDataDTO.setAdPrice(adData.getAdPrice());
			adTableDataDTO.setHouseTitle(adData.getHouse().getTitle());
			adTableDataDTO.setIsPaid(adData.getIsPaid());
			adTableDataDTO.setOrderId(adData.getOrderId());
			adTableDataDTO.setPaidDate(adData.getPaidDate());
			
			adTableDataList.add(adTableDataDTO);
		}
		
		return adTableDataList;
	}
}
