package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.helper.GoogleApiConfig;


@Controller
public class CommonController {
	

	@Autowired
	private GoogleApiConfig googleApiConfig;
	

	@GetMapping("/")
	public String home(Model model) {
		
		model.addAttribute("googleApiKey", googleApiConfig.getGoogleMapKey());
		return "homePage";
	}
    @GetMapping("/login")
    public String login(Model model) {
        return "users/login"; // 渲染 src/main/resources/templates/users/login.html
    }

    /**
     * @param model 123
     * @return
     */
    @GetMapping("/Collect")
    public String collect(Model model) {
        return "housec" +
                "ollect"; // 渲染 src/main/resources/templates/users/login.html
    }
    
}

