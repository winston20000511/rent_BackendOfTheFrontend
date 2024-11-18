package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AdCreationRequestDTO;
import com.example.demo.model.AdBean;
import com.example.demo.model.AdtypeBean;
import com.example.demo.repository.AdRepository;
import com.example.demo.repository.AdtypeRepository;

@Service
public class AdService {
	
	private AdRepository adRepository;
	private AdtypeRepository adtypeRepository;
	
	@Autowired
	public AdService(AdRepository adRepository, AdtypeRepository adtypeRepository) {
		this.adRepository = adRepository;
		this.adtypeRepository = adtypeRepository;
	}
	
	// get ads by user id and page
	public List<AdBean> findAdsByUserIdAndPage(Integer userId, Integer pageNumber){
		Pageable pageable = PageRequest.of(pageNumber-1, 10, Sort.Direction.DESC, "houseId");
		List<AdBean> ads = adRepository.findAdsByUserIdAndPage(userId, pageable);
		 System.out.println(ads);
		 return ads;
	}
	
	
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
	
}
