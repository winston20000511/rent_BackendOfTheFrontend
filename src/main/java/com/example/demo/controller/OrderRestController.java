package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OrderResponseDTO;
import com.example.demo.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

	private OrderService orderService;
	
	@Autowired
	private OrderRestController(OrderService orderService) {
		this.orderService = orderService;
	}
	
//	// get orders by userId and pageNumber 測OK
//	@GetMapping("/list/{pageNumber}")
//	public ResponseEntity<?> findOrdersByUserIdAndPageNumber(
//			@PathVariable("pageNumber") Integer pageNumber){
//		
//		// 測試資料
//		Long userId = 1L;
//		
//		if(pageNumber == null) pageNumber = 1;
//		List<OrderBean> orders = orderService.findOrdersByUserIdAndPageNumber(userId, pageNumber);
//		
//		for(OrderBean order : orders) {
//			System.out.println(order.getMerchantTradNo());
//		}
//		return ResponseEntity.ok(orders);
//		
//		/*
//		 * verify => 之後要修改 
//		 * 1. 新增session驗證, 參數加上 HttpSession session 
//		 * 2.新增錯誤訊息 
//		 * 3. ResponseEntity導回登入畫面 
//		 *
//		 * String userId = (String)session.getAttribute("userId");
//		 * if(userId == null) {
//		 * // 之後製作禁止修改訂單的錯誤訊息頁面 + 前端網頁控管
//		 * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("您沒有瀏覽權限");
//		 * }
//		*/
//		
//		/* 
//		 * 3. 重新導向 
//		 * HttpHeaders headers = new HttpHeaders();
//		 * headers/add("Location", "/users/login"); 登入頁面的路徑
//		 * new ResponseEntity<>(headers, HttpSession.FOUND);
//		 * 
//		 */
//	}
	
	
	// 要加上 user id
	@PostMapping("/filter")
	public Page<OrderResponseDTO> filterOrders(@RequestBody Map<String, String> conditions){
		return orderService.findOrdersByConditions(conditions);
	}
	
	@PostMapping("/merchantTradNo")
	public OrderResponseDTO findOrderByMerchantTradNo(@RequestBody String merchantTradNo) {
		return orderService.findOrdersByMerchantTradNo(merchantTradNo);
	}
	
	@PutMapping("/merchantTradNo")
	public boolean cancelOrderByMerchantTradNo(@RequestBody String merchantTradNo) {
		boolean result = orderService.cancelOrderByMerchantTradNo(merchantTradNo);
		return result;
	}
	
	// 接收 form 表單資料
	@PostMapping("/ecpayCheckout")
	public String ecpayCheckout() {
		String aioCheckOutALLForm = orderService.ecpayCheckout();	
		return aioCheckOutALLForm;
	}
	
}
