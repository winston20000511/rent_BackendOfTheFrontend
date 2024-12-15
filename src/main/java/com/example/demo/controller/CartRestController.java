package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.CartItemBean;
import com.example.demo.service.CartService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/cart")
public class CartRestController {

	private CartService cartService;
	
	@Autowired
	private CartRestController(CartService cartService) {
		this.cartService = cartService;
	}
	
	// 取得購物車內容
	@GetMapping("/ads")
	public ResponseEntity<List<Long>> getCartItemIds(Long userId){
		userId = 1L; //測試資料
		List<Long> addedAdIds = cartService.getCartItems(userId);
		return ResponseEntity.ok(addedAdIds);
	}
	
	@PostMapping("/list")
	public List<CartItemBean> getCartItems(Long userId){
		userId = 1L; //測試資料
		return cartService.findCartItemsByUserId(userId);
	}
	
	// 新增購物車內容
	@PostMapping("/additem")
	public boolean addToCart(@RequestBody Long adId) {
		// ad id(直接取當前資料庫儲存的資料), cart id, user id(HTTP)
		Long userId = adId; //測試資料
		System.out.println("ad id: " + adId + " user id: " + userId);
		return cartService.addToCart(adId, userId);
	}
	
	// 接收客戶點選付款後，帶入的購物車內容
	@GetMapping("/getcartcontent")
	@ResponseBody
	public Map<String, String> getCartParamForOrder(HttpSession session){
		String paymentMethod = (String) session.getAttribute("paymentMethod");
		String cartId = (String) session.getAttribute("cartId");
		Map<String, String> response = new HashMap<>();
		response.put("paymentMethod", paymentMethod != null? paymentMethod : "no method selected");
		response.put("cartId", cartId != null? cartId : "no cartId");
		
		return response;
	}
	
	// 刪除購物車內容物
	@DeleteMapping("/deleteitem")
	public boolean deleteCartItem(@RequestBody Long adId) {
		Long userId = adId; //測試資料
		return cartService.deleteCartItem(adId, userId);
	}
	
	// 刪除購物車
	// 1. 使用者自行清空  2. 使用者送出訂單 3. 時間到清空
	@DeleteMapping()
	public boolean deleteCart(@RequestBody Integer cartId) {
		return cartService.deleteCart(cartId);
	}
	
}
