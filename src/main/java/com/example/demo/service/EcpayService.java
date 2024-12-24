package com.example.demo.service;

import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.example.demo.helper.EcpayApiConfig;
import com.example.demo.model.AdBean;
import com.example.demo.model.OrderBean;
import com.example.demo.repository.OrderRepository;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@Service
public class EcpayService {
	
	private Logger logger = Logger.getLogger(EcpayService.class.getName());
	
	private OrderRepository orderRepository;
	private EcpayApiConfig ecpayApiConfig;
	private AdService adService;
	
	public EcpayService(
			OrderRepository orderRepository, EcpayApiConfig ecpayApiConfig, AdService adService) {
		this.orderRepository = orderRepository;
		this.ecpayApiConfig = ecpayApiConfig;
		this.adService = adService;
	}

	/**
	 * 製作綠界所需的訂單資料，提交並取要提交給綠界的表單格式
	 * @param merchantTradNo
	 * @return form
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
	public boolean verifyEcpayCheckValue(String returnValue) {
		
		try {
			
			String hashKey = ecpayApiConfig.getHashKey(); 
			String hashIV = ecpayApiConfig.getHashIV();
			
			// 抓出綠界回傳值中的 checkMacValue 及 MerchantTradeNo
			JSONObject queryStringToJson = queryStringToJson(returnValue);
			String merchantTradNo = (String) queryStringToJson.get("MerchantTradeNo");
			// 正式生產環境中，需要自己生成驗證碼並送給綠界，再和綠界回傳的驗證碼進行核對
			// String returnedCheckMacValue = (String) queryStringToJson.get("CheckMacValue");
			
			// 與資料庫資料比對
			OrderBean order = orderRepository.findByMerchantTradNo(merchantTradNo);
			
			order.setReturnValue(returnValue);
			order.setOrderStatus((short)1);
			
			List<AdBean> ads = adService.updateAdBeansAfterPaymentVerified(order.getAds(), order);
			order.setAds(ads);
			
			orderRepository.save(order);
			
			return true;
		}catch(Exception exception) {
			logger.info("驗證失敗");
		}

		return false;
	}
	
	/**
	 * 取得以資料庫訂單內容建立的，給綠界的訂單資料
	 * @param order
	 * @return
	 */
	private Object getEcpayOrderObj(OrderBean order) {
		AioCheckOutALL obj = new AioCheckOutALL();
		obj.setIgnorePayment("ApplePay#WebATM#ATM#CVS#BARCODE#TWQR#BNPL");

		obj.setMerchantID(ecpayApiConfig.getMerchantId());
		obj.setMerchantTradeNo(order.getMerchantTradNo());

		// 寫時間格式轉換工具
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		String dateTimeStr = order.getMerchantTradDate().format(formatter);
		obj.setMerchantTradeDate(dateTimeStr);

		// 抓cartItem計算出來的價格總計
		obj.setTotalAmount(order.getTotalAmount().toString());

		// 其他需要的資料設定
		obj.setTradeDesc(order.getTradeDesc());
		obj.setItemName(order.getItemName());
		
		// 接收回傳驗證碼路徑: 要用https回傳
		obj.setReturnURL("/api/ecpay/verify/checkvalue");		
		obj.setNeedExtraPaidInfo("N");
		
		// 返回商店後呈現給客戶看的頁面
		obj.setClientBackURL("http://localhost:5173/order-complete");
		
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
	
}
