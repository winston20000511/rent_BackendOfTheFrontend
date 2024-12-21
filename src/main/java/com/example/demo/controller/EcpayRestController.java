package com.example.demo.controller;

import java.util.logging.Logger;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.EcpayService;

@RestController
@RequestMapping("/api/ecpay")
public class EcpayRestController {

	private Logger logger = Logger.getLogger(EcpayRestController.class.getName());
	private EcpayService ecpayService;
	
	public EcpayRestController(EcpayService ecpayService) {
		this.ecpayService = ecpayService;
	}
	
	/**
	 * 送出資料並取得要送給綠界的表單格式（由前端提交）
	 * @param merchantTradNo
	 * @return
	 */
	@PostMapping("/ecpayCheckout")
	public String ecpayCheckout(@RequestBody String merchantTradNo) {
		logger.info("訂單號碼: " + merchantTradNo);
		String form =  ecpayService.ecpayCheckout(merchantTradNo);	
		return form;
	}
	
	/**
	 * 驗證綠界的回傳值
	 * @param params
	 * @return
	 */
	@PostMapping("/verify/checkvalue")
	public String verify(@RequestBody String response) {
		logger.info("綠界的回應: " + response);
		
		if(response != null) {
			boolean result = ecpayService.verifyEcpayCheckValue(response);
			
			if(result) {
				logger.info("驗證成功");
			}else {
				logger.info("驗證失敗");
			}
		}
		
		return "1|OK";
	}
	
	
}
