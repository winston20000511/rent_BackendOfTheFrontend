package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.service.AdService;

@Controller
@RequestMapping("advertisements")
public class AdController {

	private AdService adService;

	@Autowired
	public AdController(AdService adService) {
		this.adService = adService;
	}
	
	@GetMapping("/mylist")
	public String getMyAds() {
		
		/*Long loginUserId = (Long)session.getAttribute("loginUserId");
		if(loginUserId == null || userId == null || userId != loginUserId) {
			return "redirect:/loginView";
		}*/
		
		return "/adPage.html";
	}
	
}
