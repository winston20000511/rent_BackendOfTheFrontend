package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.UserTableBean;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;


@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
	@GetMapping("/users/login")
	public String login() {
		return "loginView";
	}	

	@PostMapping("/users/loginPost")
	public String loginPost(String username, String password, HttpSession httpSession, Model model) {

		Optional<UserTableBean> result = userService.checkLoginOK(username, password);
		
		if (result.isPresent()) {
			httpSession.setAttribute("loginUserId", result.get().getUserId());
			httpSession.setAttribute("loginUsername", result.get().getName());
			httpSession.setAttribute("loginUserEmail", result.get().getEmail());
			model.addAttribute("loginOkMsg", "登入成功");
			return "redirect:/";
			
		} else {
			model.addAttribute("errorMsg", "帳密錯誤");
			return "loginView";
		}
		
		
	}
	
	
}
