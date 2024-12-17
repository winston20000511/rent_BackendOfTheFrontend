package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/orders")
public class OrderController {
	
	@GetMapping("/mylist")
	public String findMyOrderList() {
		return "orderPage";
	}

	@GetMapping("/confirmpage")
    public String showOrderConfirmationPage() {
        return "orderConfirmationPage";
    }
	
	@PostMapping("/confirm")
	@ResponseBody
	public Map<String, String> confirmToPay(HttpSession session, @RequestBody Map<String, String> param) {
		// paymentMethod, cartId
		System.out.println("payment method: " + param.get("paymentMethod") + ", cart id: " + param.get("cartId"));
		
		session.setAttribute("paymentMethod", param.get("paymentMethod"));
	    session.setAttribute("cartId", param.get("cartId"));
	    
	    Map<String, String> response = new HashMap<>();
	    response.put("redirectURL", "/orders/confirmpage");
	    
	    return response;
	}
	
	@PostMapping("/complete")
	public String complete() {
		return "orderConfirmationCompletePage";
	}
	
}
