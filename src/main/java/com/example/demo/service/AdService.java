package com.example.demo.service;

import java.util.List;

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
	
	// get ads by user id (and page)
	public List<AdBean> findAdsByUserId(Integer userId){
		return adBeanDao.findAdsByUserId(userId);
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
