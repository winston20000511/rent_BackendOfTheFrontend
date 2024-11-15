package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.model.AdBean;
import com.example.demo.repository.AdRepository;

@Service
public class AdService {
	
	private AdRepository adRepository;
	
	@Autowired
	public AdService(AdRepository adRepository) {
		this.adRepository = adRepository;
	}
	
	// get ads by page & user id
	public Page<AdBean> getAdsByPageNumber(Integer pageNumber){
		PageRequest pageRequest = PageRequest.of(pageNumber-1, 20, Sort.Direction.DESC, "createdDate");
		Page<AdBean> adPages = adRepository.findAllAdsByPage(pageNumber, pageRequest);
		// List<AdBean> pageContent = adPages.getContent();
		return adPages;
	}
	
	// get an ad
	public AdBean getAdById(Integer adId) {
		Optional<AdBean> option = adRepository.findById(adId);
		if(option.isPresent()) {
			AdBean dbAd = option.get();
			return dbAd;
		}
		
		return null;
	}
	
	// create an ad new ad
	public AdBean createAd(AdBean adBean) {
		AdBean newAd = adRepository.save(adBean);
		return newAd;
	}
	
	// udpate an ad
	public AdBean updateAdById(Integer adId) {
		Optional<AdBean> option = adRepository.findById(adId);
		if(option.isPresent()) {
			AdBean adBean = option.get();
			/*設定會更新到的內容*/
			// adBean.set
			// adBean.set
			adRepository.save(adBean);
		}
		
		return null;
	}
	
	// delete an order by id
	public boolean deleteAdById(Integer adId) {
		Optional<AdBean> option = adRepository.findById(adId);
		if(option.isPresent()) {
			AdBean adBean = option.get();
			adRepository.delete(adBean);
			return true;
		}
		
		return false;
	}
	
}
