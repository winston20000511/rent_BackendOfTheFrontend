package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.OrderBean;
import com.example.demo.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private OrderService orderService;
	
	@Autowired
	private OrderController(OrderService orderService) {
		this.orderService = orderService;
	}
	
	// get orders by userId and pageNumber
	@GetMapping("/{userId}/{pageNumber}")
	public ResponseEntity<?> findOrdersByUserIdAndPageNumber(
			@PathVariable("userId") Integer userId, @PathVariable("pageNumber") Integer pageNumber){
		
		List<OrderBean> orders = orderService.findOrdersByUserIdAndPageNumber(userId, pageNumber);
		return ResponseEntity.ok(orders);
		
		/*
		 * verify => 之後要修改 
		 * 1. 新增session驗證, 參數加上 HttpSession session 
		 * 2.新增錯誤訊息 
		 * 3. ResponseEntity導回登入畫面 
		 *
		 * String userId = (String)session.getAttribute("userId");
		 * if(userId == null) {
		 * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("您沒有瀏覽權限");
		 * }
		*/
		
		/* 
		 * 3. 重新導向 
		 * HttpHeaders headers = new HttpHeaders();
		 * headers/add("Location", "/login"); 登入頁面的路徑
		 * new ResponseEntity<>(headers, HttpSession.FOUND);
		 * 
		 */
	}
	
	// get orders by merchantTradNo
	@GetMapping("/merchantTradNo/{userId}/{merchantTradNo}")
	public List<OrderBean> findOrdersByUserIdAndMerchantTradNo(
			@PathVariable("userId") Integer userId, @PathVariable("merchantTradNo") String merchantTradNo){
		List<OrderBean> orders = orderService.findOrdersByUserIdAndMerchantTradNo(userId, merchantTradNo);
		return orders;
	}
	
	// create a new order
	@PostMapping
	public OrderBean createOrder(@RequestBody OrderBean orderBean) {
		return orderService.createOrder(orderBean);
	}
	
	// cancel an order by merchantTradNo
	@PutMapping("{merchantTradNo}")
	public OrderBean cancelOrderByMerchantTradNo(@PathVariable("merchantTradNo") String merchantTradNo) {
		return orderService.cancelOrderByMerchantTradNo(merchantTradNo);
	}
	
	
	// 接收 form 表單資料
	@PostMapping("/ecpayCheckout")
	public String ecpayCheckout() {
		String aioCheckOutALLForm = orderService.ecpayCheckout();	
		return aioCheckOutALLForm;
	}
	
}
