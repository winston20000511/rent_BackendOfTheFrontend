package com.example.demo.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.helper.JwtUtil;
import com.example.demo.model.CartItemBean;
import com.example.demo.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartRestController {

	private Logger logger = Logger.getLogger(CartRestController.class.getName());
	private CartService cartService;
	
	public CartRestController(CartService cartService) {
		this.cartService = cartService;
	}
	
	@PostMapping("/list")
	public List<CartItemBean> getCartItems(@RequestHeader("authorization") String authorizationHeader){
		
		String[] userInfo = JwtUtil.verify(authorizationHeader);
		Long userId = Long.parseLong(userInfo[1]);

		List<CartItemBean> cartItems = cartService.findCartItemsByUserId(userId);
		logger.severe("購物車內容: " + cartItems);
		if(cartItems == null) return null;
		
		for(CartItemBean cartItem : cartItems) {
			logger.severe("購物車編號: " + cartItem.getCartId().toString());
		}
		
		return cartItems;
	}
	
	@PostMapping("/add/item")
	public boolean addToCart(
			@RequestBody Long adId, @RequestHeader("authorization") String authorizationHeader) {
		
		String[] userInfo = JwtUtil.verify(authorizationHeader);
		Long userId = Long.parseLong(userInfo[1]);
		
		return cartService.addToCart(userId, adId);
	}
	
	@Transactional
	@DeleteMapping("/delete/item")
	public boolean deleteCartItem(
			@RequestBody Long adId, @RequestHeader("authorization") String authorizationHeader) {
		
		String[] userInfo = JwtUtil.verify(authorizationHeader);
		Long userId = Long.parseLong(userInfo[1]);
		
		return cartService.deleteCartItem(userId, adId);
	}
	
	// 1. 金流驗證沒問題 2. 時間到清空
	@Transactional
	@DeleteMapping()
	public boolean deleteCart(
			@RequestBody Integer cartId, @RequestHeader("authorization") String authorizationHeader) {
		
		String[] userInfo = JwtUtil.verify(authorizationHeader);
		Long userId = Long.parseLong(userInfo[1]);
		
		return cartService.deleteCart(userId, cartId);
	}

	
	@GetMapping("/coupon")
	public Byte getUserCouponNumber(@RequestHeader("authorization") String authorizationHeader) {
		
		String[] userInfo = JwtUtil.verify(authorizationHeader);
		Long userId = Long.parseLong(userInfo[1]);
		
		return cartService.getUserCouponNumber(userId);
	}
	
//	@DeleteMapping("/coupon/remove")
//	public boolean removeOneCoupon(@RequestHeader("authorization") String authorizationHeader) {
//		
//		String[] userInfo = JwtUtil.verify(authorizationHeader);
//		Long userId = Long.parseLong(userInfo[1]);
//		
//		return cartService.removeOneCoupon(userId);
//		
//	}
	

}
