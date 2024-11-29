package com.example.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OrderResponseDTO;
import com.example.demo.dto.OrderSearchRequestDTO;
import com.example.demo.model.OrderBean;
import com.example.demo.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

	private OrderService orderService;
	
	@Autowired
	private OrderRestController(OrderService orderService) {
		this.orderService = orderService;
	}
	
	// get orders by userId and pageNumber 測OK
	@GetMapping("/{pageNumber}")
	public ResponseEntity<?> findOrdersByUserIdAndPageNumber(
			@PathVariable("pageNumber") Integer pageNumber){
		
		// 測試資料
		Long userId = 1L;
		
		if(pageNumber == null) pageNumber = 1;
		List<OrderBean> orders = orderService.findOrdersByUserIdAndPageNumber(userId, pageNumber);
		
		for(OrderBean order : orders) {
			System.out.println(order.getMerchantTradNo());
		}
		return ResponseEntity.ok(orders);
		
		/*
		 * verify => 之後要修改 
		 * 1. 新增session驗證, 參數加上 HttpSession session 
		 * 2.新增錯誤訊息 
		 * 3. ResponseEntity導回登入畫面 
		 *
		 * String userId = (String)session.getAttribute("userId");
		 * if(userId == null) {
		 * // 之後製作禁止修改訂單的錯誤訊息頁面 + 前端網頁控管
		 * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("您沒有瀏覽權限");
		 * }
		*/
		
		/* 
		 * 3. 重新導向 
		 * HttpHeaders headers = new HttpHeaders();
		 * headers/add("Location", "/users/login"); 登入頁面的路徑
		 * new ResponseEntity<>(headers, HttpSession.FOUND);
		 * 
		 */
	}
	
	@PostMapping("/filter") // 之後加pageNumber
	public List<OrderResponseDTO> filterOrders(@RequestBody Map<String, String> selectedConditions){
		OrderSearchRequestDTO requestDTO = new OrderSearchRequestDTO();
		Long userId = 2L; //測試資料
		String status = selectedConditions.get("status");
		String condition = selectedConditions.get("condition");
		String input = selectedConditions.get("input");

		requestDTO.setUserId(userId);
		requestDTO.setOrderStatus(status);
		
		switch(condition) {
			case "merchantTradNo":
				requestDTO.setMerchantTradNo(input); break;
			case "period":
				String[] dates = input.split(" to ");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                requestDTO.setStartDate(LocalDateTime.parse(dates[0], formatter));
                requestDTO.setEndDate(LocalDateTime.parse(dates[1], formatter));
                break;
			case "houseTitle":
				requestDTO.setHouseTitle(input);
				break;
		}
		
		return orderService.findOrdersByConditions(requestDTO);
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
