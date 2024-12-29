package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CartItemResponseDTO;
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
	public List<CartItemResponseDTO> getCartItems(@RequestHeader("authorization") String authorizationHeader){
		
		String[] userInfo = JwtUtil.verify(authorizationHeader);
		Long userId = Long.parseLong(userInfo[1]);
		
		List<CartItemBean> cartItems = cartService.findCartItemsByUserId(userId);
		if(cartItems == null) return null;

		List<CartItemResponseDTO> responseDTOs = new ArrayList<>();
		for(CartItemBean cartItem : cartItems) {
			CartItemResponseDTO responseDTO = new CartItemResponseDTO();
			responseDTO.setAdId(cartItem.getAdId());
			responseDTO.setCartId(cartItem.getCartId());
			responseDTO.setHouseTitle(cartItem.getAd().getHouse().getTitle());
			responseDTO.setAdName(cartItem.getAdtype().getAdName());
			responseDTO.setAdPrice(cartItem.getAdPrice());
			responseDTO.setAddedDate(cartItem.getAddedDate());
			
			responseDTOs.add(responseDTO);
		}
		
		return responseDTOs;
	}
	
	@PostMapping("/add/item")
	public boolean addToCart(
			@RequestBody Long adId, @RequestHeader("authorization") String authorizationHeader) {
		
		String[] userInfo = JwtUtil.verify(authorizationHeader);
		Long userId = Long.parseLong(userInfo[1]);
		
		return cartService.addToCart(userId, adId);
	}
	
	@DeleteMapping("/delete/item")
	public boolean deleteCartItem(
			@RequestBody Long adId, @RequestHeader("authorization") String authorizationHeader) {
		
		String[] userInfo = JwtUtil.verify(authorizationHeader);
		Long userId = Long.parseLong(userInfo[1]);
		
		return cartService.deleteCartItem(userId, adId);
	}
	
	@GetMapping("/coupon")
	public Byte getUserCouponNumber(@RequestHeader("authorization") String authorizationHeader) {
		
		String[] userInfo = JwtUtil.verify(authorizationHeader);
		Long userId = Long.parseLong(userInfo[1]);
		
		return cartService.getUserCouponNumber(userId);
	}

}
