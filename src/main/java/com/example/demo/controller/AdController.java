package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.service.AdService;

@Controller
public class AdController {

	private AdService adService;

	@Autowired
	public AdController(AdService adService) {
		this.adService = adService;
	}
	
	@GetMapping("/advertisements/mylist")
	public String getMyAds() {
		
		/*Long loginUserId = (Long)session.getAttribute("loginUserId");
		if(loginUserId == null || userId == null || userId != loginUserId) {
			return "redirect:/loginView";
		}*/
		
		return "/adPage.html";
	}
}
