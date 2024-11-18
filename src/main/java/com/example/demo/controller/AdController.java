package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.AdBean;
import com.example.demo.service.AdService;

@RestController
@RequestMapping("/advertisements")
public class AdController {

	@Autowired
	private AdService adService;
	
	
	// get ads by user id (and page)
	@GetMapping("/{userId}")
	public List<AdBean> findAdsByUserId(Integer userId) {
		return adService.findAdsByUserId(userId);
	}
	
	// create new ad
	@PostMapping
	@Transactional
	public AdBean ceateAd(AdBean adBean) {
		return adService.createAd(adBean);
	}
	
	// update an ad by house id and order id
	@PostMapping("/{adId}")
	public AdBean updateAdById(Integer adId) {
		return adService.updateAdById(adId);
	}
	
	// delete an ad
	@Transactional
	public boolean deleteAdById(Integer adId) {
		return adService.deleteAdById(adId);
	}
}
