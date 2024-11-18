package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.AdBean;
import com.example.demo.repository.AdBeanDao;

@Service
public class AdService {
	
	private AdBeanDao adBeanDao;
	
	@Autowired
	public AdService(AdBeanDao adBeanDao) {
		this.adBeanDao = adBeanDao;
	}
	
	// get ads by page & user id
	
	
	// get an ad
	public AdBean getAdById(Integer adId) {
		return null;
	}
	
	// create an ad new ad
	public AdBean createAd(AdBean adBean) {
		return null;
	}
	
	// udpate an ad
	public AdBean updateAdById(Integer adId) {
		return null;
	}
	
	// delete an order by id
	public boolean deleteAdById(Integer adId) {
		return false;
	}
	
}
