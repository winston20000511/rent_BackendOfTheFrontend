package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OrderConfirmationResponseDTO;
import com.example.demo.dto.OrderCreationRequestDTO;
import com.example.demo.dto.OrderResponseDTO;
import com.example.demo.helper.JwtUtil;
import com.example.demo.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

	private Logger logger = Logger.getLogger(OrderResponseDTO.class.getName());
	
	private OrderService orderService;
	
	public OrderRestController(OrderService orderService) {
		this.orderService = orderService;
	}

	/**
	 * 接收使用者端傳來的篩選條件 conditions:
	 * String page
	 * String status(all/cancelling/active/cancelled)
	 * String daterange(all/week/month/year)
	 * String inputcondition(none/housetitle/merchantTradNo)
	 * String input
	 * 
	 * @param conditions
	 * @param authorizationHeader
	 * @return
	 */
	@PostMapping("/filter")
	public Page<OrderResponseDTO> filterOrders(
			@RequestBody Map<String, String> conditions, @RequestHeader("authorization") String authorizationHeader){

		String[] userInfo = JwtUtil.verify(authorizationHeader);
		Long userId = Long.parseLong(userInfo[1]);
		
		return orderService.findOrdersByConditions(userId, conditions);
	}
	
	@Transactional
	@PostMapping("/create")
	public OrderResponseDTO createOrder(
			@RequestBody OrderCreationRequestDTO requestDTO, @RequestHeader("authorization") String authorizationHeader) {
		
		String[] userInfo = JwtUtil.verify(authorizationHeader);
		Long userId = Long.parseLong(userInfo[1]);
		
		OrderResponseDTO newOrder = orderService.createOrder(userId, requestDTO);
		
		return newOrder;
	}
	
	@PostMapping("/merchantTradNo")
	public OrderResponseDTO findOrderByMerchantTradNo(
			@RequestBody String merchantTradNo, @RequestHeader("authorization") String authorizationHeader) {
		
		String[] userInfo = JwtUtil.verify(authorizationHeader);
		Long userId = Long.parseLong(userInfo[1]);
		
		return orderService.findOrdersByMerchantTradNo(userId, merchantTradNo);
	}
	
	@PutMapping("/merchantTradNo")
	public boolean cancelOrderByMerchantTradNo(
			@RequestBody String merchantTradNo, @RequestHeader("authorization") String authorizationHeader) {
		
		String[] userInfo = JwtUtil.verify(authorizationHeader);
		Long userId = Long.parseLong(userInfo[1]);
		
		return orderService.cancelOrderByMerchantTradNo(userId, merchantTradNo);
	}
	
	/**
	 * 取得要讓使用者確認的訂單內容
	 * @param cartId
	 * @return 訂單確認資料
	 */
	@PostMapping("/content/confirmation")
	public List<OrderConfirmationResponseDTO> confirmOrderContent(@RequestBody Integer cartId) {
		return orderService.getOrderConfirmationResponseDTOsByCartId(cartId);
	}
	
}
