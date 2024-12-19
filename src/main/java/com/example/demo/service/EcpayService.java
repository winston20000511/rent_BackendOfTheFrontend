package com.example.demo.service;

import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.helper.EcpayApiConfig;
import com.example.demo.model.OrderBean;
import com.example.demo.repository.OrderRepository;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

@Service
public class EcpayService {
	
	private Logger logger = Logger.getLogger(EcpayService.class.getName());
	
	private OrderRepository orderRepository;
	private EcpayApiConfig ecpayApiConfig;
	
	@Autowired
	public EcpayService(OrderRepository orderRepository, EcpayApiConfig ecpayApiConfig) {
		this.orderRepository = orderRepository;
		this.ecpayApiConfig = ecpayApiConfig;
	}

	/**
	 * 將訂單資料送給綠界
	 * @param merchantTradNo
	 * @return form 綠界的付款回傳表單
	 */
	public String ecpayCheckout(String merchantTradNo) {

		OrderBean order = orderRepository.findByMerchantTradNo(merchantTradNo);
		AllInOne all = new AllInOne("");
		Object obj = getEcpayOrderObj(order);
		String form = all.aioCheckOut(obj, null);
		return form;
	}

	/**
	 * 接收綠界回傳的值
	 * @param response
	 * @return 1/OK 通知綠界有收到驗證資料
	 */
	public String verifyEcpayCheckValue(String response) {
		logger.severe("response: " + response);
		System.out.println(response);
		// 抓出綠界回傳值中的checkMacValue 及 MerchantTradeNo 
		String hashKey = ecpayApiConfig.getHashKey(); 
		String hashIV = ecpayApiConfig.getHashIV();
		
		
		JSONObject queryStringToJson = queryStringToJson(response);
		queryStringToJson.toString(); // 存入資料庫
		
		/*
		OrderBean order = orderRepository.findByMerchantTradNo(merchantTradNo);
		Object ecpayOrderObj = getEcpayOrderObj(order);
		
		String genMheckMacValue = EcpayEncrpytUtil.getCheckMacValue(hashKey, hashIV, ecpayOrderObj);
		logger.severe("自己生成的checkMacValue: " + genMheckMacValue);
		
//		if response 的 checkMacValue.equals(checkMacValue) 將order status改為1 ; else 不做動作
		*/
		
		return "1|OK"; // 單純回報有收到確認碼
	}
	
	/**
	 * 取得以資料庫訂單內容建立的，給綠界的訂單資料
	 * @param order
	 * @return
	 */
	private Object getEcpayOrderObj(OrderBean order) {
		AioCheckOutALL obj = new AioCheckOutALL();
		obj.setIgnorePayment("ApplePay#WebATM#ATM#CVS#BARCODE#TWQR#BNPL");

		obj.setMerchantTradeNo(order.getMerchantTradNo());

		// 寫時間格式轉換工具
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String dateTimeStr = order.getMerchantTradDate().format(formatter);
		obj.setMerchantTradeDate(dateTimeStr);

		// 抓cartItem計算出來的價格總計
		obj.setTotalAmount(order.getTotalAmount().toString());

		// 其他需要的資料設定
		obj.setTradeDesc(order.getTradeDesc());
		obj.setItemName(order.getItemName());
		
		// 接收回傳驗證碼路徑
		obj.setReturnURL("/api/ecpay/verify/checkvalue");
		
		obj.setNeedExtraPaidInfo("N");
		
		// 返回商店後呈現給客戶看的葉面
		obj.setClientBackURL("http://localhost:8080/orders/complete");
		
		return obj;
	}
	
	private JSONObject queryStringToJson(String queryString) {
		JSONObject jsonObject = new JSONObject();
		try {
			String[] pairs = queryString.split("&");
			
			for(String pair : pairs) {
				String[] keyValue = pair.split("=");
				
				if(keyValue.length == 2) {
					String key = URLDecoder.decode(keyValue[0], "UTF-8");
					String value = URLDecoder.decode(keyValue[1], "UTF-8");
					jsonObject.put(key, value);
				}else {
					String key = URLDecoder.decode(keyValue[0], "UTF-8");
					jsonObject.put(key, "");
				}
			}
		}catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		
		return jsonObject;
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
		obj.setReturnURL("https://4c2c-58-114-167-137.ngrok-free.app/api/ecpay/verify/checkvalue");
		obj.setClientBackURL("/orders/complete");
		obj.setNeedExtraPaidInfo("N");
		String form = all.aioCheckOut(obj, null);
		return form;
	}
}
