package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.CartItemBean;
import com.example.demo.model.OrderBean;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.OrderRepository;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;

@Service
public class EcpayService {
	
	private OrderRepository orderRepository;
	
	@Autowired
	private EcpayService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}
	
	
	/* ecpay */
	// 測試用
	public String testEcpayCheckout() {

		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
		AllInOne all = new AllInOne("");
		AioCheckOutALL obj = new AioCheckOutALL();
		obj.setMerchantTradeNo(uuId);
		obj.setIgnorePayment("WebATM#ATM#BNPL#ApplePay#TWQR");
		obj.setMerchantTradeDate("2024/12/10 08:05:23");
		obj.setTotalAmount("50");
		obj.setTradeDesc("test Description");
		obj.setItemName("TestItem");
//		obj.setOrderResultURL("/orders/complete");
		obj.setReturnURL("https://seriously-smooth-turkey.ngrok-free.app/api/ecpay/verify/checkvalue");
//		obj.setReturnURL("<http://211.23.128.214:5000>");
		obj.setClientBackURL("https://seriously-smooth-turkey.ngrok-free.app/orders/complete");
		obj.setNeedExtraPaidInfo("N");
		String form = all.aioCheckOut(obj, null);
		return form;

		/*
		 * AllInOne all = new AllInOne(""); AioCheckOutALL obj = new AioCheckOutALL();
		 * obj.setIgnorePayment("ApplePay#WebATM");
		 * obj.setMerchantTradeNo("textCompany1013");
		 * obj.setMerchantTradeDate("2024/11/18 08:05:23"); obj.setTotalAmount("50");
		 * obj.setTradeDesc("test Description"); obj.setItemName("TestItem"); //
		 * 交易結果回傳網址，只接受 https 開頭的網站，可以使用 ngrok //
		 * obj.setReturnURL("<http://211.23.128.214:5000>"); obj.setReturnURL("OK");
		 * obj.setNeedExtraPaidInfo("N"); // 商店轉跳網址 //
		 * obj.setClientBackURL("<http://192.168.1.37:8080/>");
		 * obj.setClientBackURL("http://localhost:8080/complete"); String form =
		 * all.aioCheckOut(obj, null); return form;
		 */
	}

	public String ecpayCheckout(String merchantTradNo) {
		// 前端傳入值：cartId, paymentMethod

		OrderBean order = orderRepository.findByMerchantTradNo(merchantTradNo);
		
		AllInOne all = new AllInOne("");
		AioCheckOutALL obj = new AioCheckOutALL();
		obj.setIgnorePayment("ApplePay#WebATM#ATM#CVS#BARCODE#TWQR#BNPL");

		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
		// 使用訂單生成程式碼帶入
		obj.setMerchantTradeNo(uuId);
		// obj.setMerchantTradeNo(order.getMerchantTradeNo());

		// 寫時間格式轉換工具
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String dateTimeStr = LocalDateTime.now().format(formatter);
		// String dateTimeStr = order.getMerchantTradDate().format(formatter);
		
		System.out.println("dateTimeStr" + dateTimeStr);
		obj.setMerchantTradeDate(dateTimeStr);

		// 抓cartItem計算出來的價格總計
		obj.setTotalAmount(order.getTotalAmount().toString());

		obj.setTradeDesc(order.getTradeDesc());
		obj.setItemName(order.getItemName());
		obj.setReturnURL("<http://211.23.128.214:80>");
		obj.setNeedExtraPaidInfo("N");
		obj.setClientBackURL("http://localhost:8080/orders/complete");
		String form = all.aioCheckOut(obj, null);

		return form;
	}

	// get ecpay check value and verify
	public String verifyEcpayCheckValue(String checkValue) {

		System.out.println("check value: " + checkValue);
//		AllInOne all = new AllInOne("");

		return "1|OK";
	}
}
