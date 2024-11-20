package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.AdBean;
import com.example.demo.service.AdService;

@Controller
public class AdController {

	private AdService adService;

	@Autowired
	public AdController(AdService adService) {
		this.adService = adService;
	}
	
	@GetMapping("/advertisements/mylist")
	public String getMyAds(Long userId, @RequestParam(defaultValue = "1") Integer pageNumber, Model model) {
		
		/*Long loginUserId = (Long)session.getAttribute("loginUserId");
		if(loginUserId == null || userId == null || userId != loginUserId) {
			return "redirect:/loginView";
		}*/
		
		List<AdBean> ads = adService.findAdsByUserIdAndIsPaidAndPage(userId, true, pageNumber);
		for(AdBean ad : ads) {
			System.out.println(ad.getIsPaid());
		}
		model.addAttribute("ads", ads);
		return "/adPage.html";
	}
}
