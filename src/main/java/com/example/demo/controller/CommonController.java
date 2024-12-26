package com.example.demo.controller;

import com.example.demo.service.FakeDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.helper.GoogleApiConfig;

import java.io.IOException;


@Controller
public class CommonController {
	

	@Autowired
	private GoogleApiConfig googleApiConfig;

    @Autowired
    private FakeDataService fakeDataService;

	@GetMapping("/")
	public String home(Model model) {
		
		model.addAttribute("googleApiKey", googleApiConfig.getGoogleMapKey());
		return "homePage";
	}
    @GetMapping("/login")
    public String login(Model model) {
        return "users/login"; // 渲染 src/main/resources/templates/users/login.html
    }

    @GetMapping("/Collect")
    public String collect(Model model) {
        return "housecollect"; // 渲染 src/main/resources/templates/users/login.html
    }

    @GetMapping("/fake")
    public void fake() {
        try {
            fakeDataService.imageFakeData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("ok");
    }

}

