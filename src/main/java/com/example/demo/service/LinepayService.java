package com.example.demo.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.helper.LinepayApiConfig;
import com.example.demo.helper.PaymentUtil;
import com.example.demo.model.AdBean;
import com.example.demo.model.CartItemBean;
import com.example.demo.model.OrderBean;
import com.example.demo.model.linepay.CheckoutPaymentRequestForm;
import com.example.demo.model.linepay.ProductForm;
import com.example.demo.model.linepay.ProductPackageForm;
import com.example.demo.model.linepay.RedirectUrls;
import com.example.demo.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LinepayService {

	private Logger logger = Logger.getLogger(LinepayService.class.getName());
	private OrderRepository orderRepository;
	private LinepayApiConfig linpayApiConfig;
	private AdService adService;
	private CartService cartService;

	public LinepayService(
			OrderRepository orderRepository, LinepayApiConfig linpayApiConfig, AdService adService, CartService cartService) {
		this.orderRepository = orderRepository;
		this.linpayApiConfig = linpayApiConfig;
		this.adService = adService;
		this.cartService = cartService;
	}

	/**
	 * 執行送出請求
	 * @param orderId
	 * @return
	 */
	public ResponseEntity<?> processPaymentRequest(String orderId) {
		
		CheckoutPaymentRequestForm paymentForm = generatePaymentForm(orderId);

		Map<String, String> requestInfo = generateRequestInfo(paymentForm);
		String requestBody = requestInfo.get("requestBody");
		String nonce = requestInfo.get("nonce");
		String signature = requestInfo.get("signature");

		HttpHeaders headers = generateHttpHeader(nonce, signature);
		String response = sendRequest(headers, requestBody);
		String paymentURL = processResponse(response, orderId);
		
		try {
			
			if (orderId != null) {
				logger.info("回傳的路徑: " + paymentURL);
				return ResponseEntity.ok(Map.of("paymentUrl", paymentURL));
			}
		}catch(Exception exception){
			
			logger.info(exception.getMessage());
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error processing payment request: " + exception.getMessage());
		}

		return null;
	}

	/**
	 * 製作請求付款的 CheckoutPaymentRequestForm
	 * @param orderId
	 * @return
	 */
	private CheckoutPaymentRequestForm generatePaymentForm(String orderId) {

		OrderBean order = orderRepository.findByMerchantTradNo(orderId);
		
		CheckoutPaymentRequestForm form = new CheckoutPaymentRequestForm();
		form.setOrderId(orderId);
		form.setAmount(new BigDecimal(order.getTotalAmount().toString())); // amount = price * quantity
		form.setCurrency("TWD");

		ProductPackageForm productPackageForm = new ProductPackageForm();
		productPackageForm.setId(UUID.randomUUID().toString());
		productPackageForm.setName("advertisements");
		productPackageForm.setAmount(new BigDecimal(order.getTotalAmount().toString()));

		// 有時間改用stream寫寫看
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

	/**
	 * 製作LINEPAY需要的 requestInfo
	 * @param form
	 * @return
	 */
	private Map<String, String> generateRequestInfo(CheckoutPaymentRequestForm form) {

		Map<String, String> requestInfo = new HashMap<>();

		String ChannelSecret = linpayApiConfig.getLinpayChannelSecretKey();
		String requestUri = "/v3/payments/request"; // 固定的設定
		String nonce = UUID.randomUUID().toString();

		ObjectMapper mapper = new ObjectMapper();
		String requestBody;

		try {

			requestBody = mapper.writeValueAsString(form);
			String signature = PaymentUtil.encryptLinepayRequest(ChannelSecret, requestUri, requestBody, nonce);

			// 檢查資料是否正確
			logger.info("requestBody" + requestBody);
			logger.info("nonce: " + nonce);
			logger.info("signature: " + signature);

			// 可以封裝成DTO，較好看懂（有時間再寫）
			requestInfo.put("requestBody", requestBody);
			requestInfo.put("nonce", nonce);
			requestInfo.put("signature", signature);

			// 回傳資料
			return requestInfo;

		} catch (JsonProcessingException exception) {
			exception.printStackTrace();
		}

		return null;
	}

	/**
	 * 設定HTTPS Headsers
	 * @param nonce
	 * @param signature
	 * @return
	 */
	private HttpHeaders generateHttpHeader(String nonce, String signature) {

		String ChannelId = linpayApiConfig.getLinepayChannelIdKey();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-LINE-ChannelId", ChannelId);
		headers.set("X-LINE-Authorization-Nonce", nonce);
		headers.set("X-LINE-Authorization", signature);

		return headers;
	}

	/**
	 * 執行送出包含所需Headers資訊的HTTPS請求
	 * @param headers
	 * @param requestBody
	 * @return
	 */
	private String sendRequest(HttpHeaders headers, String requestBody) {

		String linepayApiUrl = "https://sandbox-api-pay.line.me/v3/payments/request"; // 固定的
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		RestTemplate restemplate = new RestTemplate();
		String response = restemplate.postForObject(linepayApiUrl, requestEntity, String.class);

		return response;
	}

	/**
	 * 處理Linepay的回傳值
	 * @param response
	 * @param orderId
	 * @return
	 */
	private String processResponse(String response, String orderId) {

		if (response == null || response.isEmpty()) {
			logger.info("沒有回應");
			return null;
		}

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonResponse;
		
		logger.info("linepay回應: " + response);

		try {

			jsonResponse = mapper.readTree(response);
			String returnCode = jsonResponse.get("returnCode").asText();
			
			// 0000 成功
			if(returnCode.equals("0000")) {
				String paymentURL = jsonResponse.get("info").get("paymentUrl").get("web").asText();
				
				// 可以做接收linepay回應的DTO
				String transactionId = jsonResponse.get("info").get("transactionId").asText();
				
				// 檢查結果
				logger.info("payment URL: " + paymentURL);
				logger.info("transaction ID: " + transactionId);
				
				updateDatabase(response, orderId);
				
				return paymentURL;
			}

		} catch (JsonProcessingException exception) {
			logger.info("LINEPAY付款失敗");
			exception.printStackTrace();
		}

		return null;
	}
	
	/**
	 * 更新資料庫中的cart、order及ad資料
	 * @param orderId
	 * @param returnValue
	 * @return
	 */
	private boolean updateDatabase(String returnValue, String orderId) {
	
		Optional<OrderBean> optional = orderRepository.findById(orderId);
		if(optional.isPresent()) {
			
			// 更新檢驗碼及訂單狀態
			OrderBean order = orderRepository.findByMerchantTradNo(orderId);
			order.setReturnValue(returnValue);
			order.setOrderStatus((short)1);
			
			List<AdBean> ads = adService.updateAdBeansAfterPaymentVerified(order.getAds(), order);
			
			order.setAds(ads);
			
			orderRepository.save(order);
			
			cartService.deleteCartItems(order.getUserId());
			cartService.deleteCart(order.getUserId());
			
			List<CartItemBean> cartItemResult = cartService.findCartItemsByUserId(order.getUserId());
			boolean deletedCart = cartService.deleteCart(order.getUserId());
			if(cartItemResult != null  && deletedCart) {
				
				return true;
			}
			
			return false;
			
		}else {
			logger.info("沒有該筆訂單資料: " + orderId);
		}
		
		return false;
	}
	
}
