package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.EcpayService;

@RestController
@RequestMapping("/api/ecpay")
public class EcpayRestController {

	private EcpayService ecpayService;
	
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
		System.out.println("訂單號碼: " + merchantTradNo);
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
		return "1|OK";
	}
	
	
}
