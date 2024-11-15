package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.AdBean;
import com.example.demo.service.AdService;

@RestController
@RequestMapping("/api/advertisements")
public class AdController {

	@Autowired
	private AdService adService;
	
	// get ads
	@GetMapping("/list")
	public Page<AdBean> getAds(Integer pageNumber) {
		return adService.getAdsByPageNumber(pageNumber);
	}
	
	// get an ad
	@GetMapping("/{adId}")
	public AdBean getAdById(Integer adId) {
		return adService.getAdById(adId);
	}
	
	// create new ad
	@PostMapping
	@Transactional
	public AdBean ceateAd(AdBean adBean) {
		return adService.createAd(adBean);
	}
	
	// update an ad
	@PostMapping("/{adId}")
	@Transactional
	public AdBean updateAdById(Integer adId) {
		return adService.updateAdById(adId);
	}
	
	// delete an ad
	@GetMapping("/{adId}")
	@Transactional
	public boolean deleteAdById(Integer adId) {
		return adService.deleteAdById(adId);
	}
}
