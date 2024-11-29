package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {
	
	@GetMapping("/orders/mylist")
	public String findMyOrderList() {
		return "orderPage";
	}

	@GetMapping("/orders/confirmpage")
	public String createOrder() {
		return "orderConfirmationPage";
	}
	
}
