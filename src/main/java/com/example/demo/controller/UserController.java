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

		UserTableBean result = userService.checkLoginOK(username, password);
		
		if (result!=null) {
			httpSession.setAttribute("loginUserId", result.getUserId());
			httpSession.setAttribute("loginUsername", result.getName());
			httpSession.setAttribute("loginUserEmail", result.getEmail());
			model.addAttribute("loginOkMsg", "登入成功");
			return "redirect:/";
			
		} else {
			model.addAttribute("errorMsg", "帳密錯誤");
			return "loginView";
		}
		
		
	}
	public UserTableBean checkLogin(String username, String password) {
        // TODO Auto-generated method stub
        return null;
	}
	
}
