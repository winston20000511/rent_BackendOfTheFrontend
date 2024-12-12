package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.OrderBean;
import com.example.demo.service.EcpayService;

@RestController
@RequestMapping("/api/ecpay")
public class EcpayRestController {

	private EcpayService ecpayService;
	
	
	@Autowired
	private EcpayRestController(EcpayService ecpayService) {
		this.ecpayService = ecpayService;
	}
	
	// 接收 form 表單資料
	@PostMapping("/test/ecpayCheckout")
	public String testEecpayCheckout() {
		String aioCheckOutALLForm = ecpayService.testEcpayCheckout();	
		return aioCheckOutALLForm;
	}
	
	@PostMapping("/ecpayCheckout")
	public String ecpayCheckout(@RequestBody String merchantTradNo) {
		String aioCheckOutALLForm = ecpayService.ecpayCheckout(merchantTradNo);	
		return aioCheckOutALLForm;
	}
	
	@PostMapping("/verify/checkvalue")
	public String verify(@RequestBody Map<String, String> params) {
		System.out.println(params.get("CheckMacValue"));
//		String response = orderService.verifyEcpayCheckValue(checkValue);
//		return response;
		return null;
	}
	
}
