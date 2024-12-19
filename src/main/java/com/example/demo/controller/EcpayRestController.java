package com.example.demo.controller;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.helper.EcpayApiConfig;
import com.example.demo.model.OrderBean;
import com.example.demo.service.EcpayService;

@RestController
@RequestMapping("/api/ecpay")
public class EcpayRestController {

	private EcpayService ecpayService;
	
	@Autowired
	public EcpayRestController(EcpayService ecpayService) {
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
//	public String verify(@RequestBody Map<String, String> params) 
	public String verify(@RequestBody String params) {
		System.out.println("印啊111");
		System.out.println(params);
		System.out.println("印啊222");
//		String response = 驗證
//		驗證後將資料庫order status 改為 1
//		if(response是true) return OK; // 回傳OK給綠界
		return null;
	}
	
	
}
