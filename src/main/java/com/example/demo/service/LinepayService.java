package com.example.demo.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.helper.HMACUtil;
import com.example.demo.helper.LinepayApiConfig;
import com.example.demo.model.AdBean;
import com.example.demo.model.OrderBean;
import com.example.demo.model.linepay.CheckoutPaymentRequestForm;
import com.example.demo.model.linepay.ProductForm;
import com.example.demo.model.linepay.ProductPackageForm;
import com.example.demo.model.linepay.RedirectUrls;
import com.example.demo.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import ch.qos.logback.core.net.ObjectWriter;

@Service
public class LinepayService {

	private OrderRepository orderRepository;
	private LinepayApiConfig linpayApiConfig;

	@Autowired
	public LinepayService(OrderRepository orderRepository, LinepayApiConfig linpayApiConfig) {
		this.orderRepository = orderRepository;
		this.linpayApiConfig = linpayApiConfig;
	}

	// 執行送出請求
	public String processPaymentRequest(String orderId) {

		CheckoutPaymentRequestForm paymentForm = generatePaymentForm(orderId);

		Map<String, String> requestInfo = generateRequestInfo(paymentForm);
		String requestBody = requestInfo.get("requestBody");
		String nonce = requestInfo.get("nonce");
		String signature = requestInfo.get("signature");

		HttpHeaders headers = generateHttpHeader(nonce, signature);

		String response = sendRequest(headers, requestBody);

		String paymentURL = processResponse(response); // 取得付款連結

		return paymentURL;
	}

	// 取得使用者買新order的資料
	// 之後再補寫傳入orderCreationRequestDTO object
	private Map<String, String> getInfoOfNewOrder(String orderId) {

		OrderBean order = orderRepository.findByMerchantTradNo(orderId);

		if (order != null) {
			Map<String, String> orderInfo = new HashMap<>();
			// 總金額
			orderInfo.put("totalAmount", order.getTotalAmount().toString());
			// 產品敘述 = product package
			orderInfo.put("itemDescription", order.getTradeDesc());
			// 產品名稱
			orderInfo.put("productPackage", order.getItemName());

			// 方案名稱 + 金額 + 數量 = ProductForm
			List<AdBean> ads = order.getAds();
			StringBuilder productNames = new StringBuilder();
			for (AdBean ad : ads) {
				// adtype.adName-1xadPrice;
				productNames.append("adId").append(ad.getAdtypeId()).append(".").append(ad.getAdtype().getAdName())
						.append("-").append("1").append("x").append(ad.getAdPrice().toString()).append(";");
				System.out.println("productNames: " + productNames);
			}

			orderInfo.put("productNames", productNames.toString());

			return orderInfo;
		}

		return null;
	}

	// 製作請求付款的 request body
	// 之後改傳整份訂單資料進來
	private CheckoutPaymentRequestForm generatePaymentForm(String orderId) {

		// 找 Order
		OrderBean order = orderRepository.findByMerchantTradNo(orderId);
		
		CheckoutPaymentRequestForm form = new CheckoutPaymentRequestForm();
		form.setOrderId(orderId);
		form.setAmount(new BigDecimal(order.getTotalAmount().toString())); // amount = price * quantity
		form.setCurrency("TWD");

		ProductPackageForm productPackageForm = new ProductPackageForm();
		productPackageForm.setId(UUID.randomUUID().toString());
		productPackageForm.setName("advertisements");
		productPackageForm.setAmount(new BigDecimal(order.getTotalAmount().toString()));

		// ADID_adtype.adName:1xadPrice;
		/*
		 * List<ProductForm> productForms = orderInfo.entrySet().stream()
		 */

		ProductForm productForm = new ProductForm();
		List<AdBean> ads = order.getAds();
		for (AdBean ad : ads) {
			productForm.setId(ad.getAdId().toString());
			productForm.setName(ad.getAdtype().getAdName());
			productForm.setQuantity(new BigDecimal("1"));
			productForm.setPrice(new BigDecimal(ad.getAdPrice()));
		}

		productPackageForm.setProducts(Arrays.asList(productForm));
		form.setPackages(Arrays.asList(productPackageForm));

		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setConfirmUrl("http://localhost:5173/order-complete");
		redirectUrls.setCancelUrl("");
		form.setRedirectUrls(redirectUrls);

		return form;
	}

	private Map<String, String> generateRequestInfo(CheckoutPaymentRequestForm form) {

		Map<String, String> requestInfo = new HashMap<>();

		String ChannelSecret = linpayApiConfig.getLinpayChannelSecretKey();
		String requestUri = "/v3/payments/request"; // 固定的設定
		String nonce = UUID.randomUUID().toString();

		ObjectMapper mapper = new ObjectMapper();
		String requestBody;

		try {

			requestBody = mapper.writeValueAsString(form);
			String signature = HMACUtil.encrypt(ChannelSecret, requestUri, requestBody, nonce);

			// 看資料是否正確
			System.out.println("requestBody" + requestBody);
			System.out.println("nonce: " + nonce);
			System.out.println("signature: " + signature);

			// 可以封裝成DTO，較好看懂（有時間再寫）
			requestInfo.put("requestBody", requestBody);
			requestInfo.put("nonce", nonce);
			requestInfo.put("signature", signature);

			// 回傳資料
			return requestInfo;

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return null;
	}

	// 設定 HttpHeaders
	private HttpHeaders generateHttpHeader(String nonce, String signature) {

		String ChannelId = linpayApiConfig.getLinepayChannelIdKey();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-LINE-ChannelId", ChannelId);
		headers.set("X-LINE-Authorization-Nonce", nonce);
		headers.set("X-LINE-Authorization", signature);

		return headers;
	}

	// 送出
	private String sendRequest(HttpHeaders headers, String requestBody) {

		String linepayApiUrl = "https://sandbox-api-pay.line.me/v3/payments/request"; // 固定的
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		RestTemplate restemplate = new RestTemplate();
		String response = restemplate.postForObject(linepayApiUrl, requestEntity, String.class);

		// 檢視資料
		System.out.println("send request and get response: " + response);

		return response;
	}

	// 處理回應
	private String processResponse(String response) {

		if (response == null || response.isEmpty()) {
			System.out.println("沒有回應");
			return null;
		}

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonResponse;
		
		System.out.println("linepay回應: " + response);

		try {

			jsonResponse = mapper.readTree(response);
			String paymentURL = jsonResponse.get("info").get("paymentUrl").get("web").asText();

			// transactionId 之後要要存到資料庫裡 => 做接收linepay回應的DTO
			String transactionId = jsonResponse.get("info").get("transactionId").asText();

			// 檢查結果
			System.out.println("payment URL: " + paymentURL);
			System.out.println("transaction ID: " + transactionId);

			return paymentURL;

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return null;
	}
}
