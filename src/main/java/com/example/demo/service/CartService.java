package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.model.AdBean;
import com.example.demo.model.CartBean;
import com.example.demo.model.CartItemBean;
import com.example.demo.repository.AdRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.UserRepository;


@Service
public class CartService {
	
	private Logger logger = Logger.getLogger(CartService.class.getName());
	
	private CartRepository cartRepository;
	private CartItemRepository cartItemRepository;
	private AdRepository adRepository;
	private UserRepository userRepository;
	
	public CartService() {
    }
	
	@Autowired
	private CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, 
			AdRepository adRepository, UserRepository userRepository) {
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
		this.adRepository = adRepository;
		this.userRepository = userRepository;
	}
	
	/**
	 * 以 userId 取得購物車內容
	 * @param userId
	 * @return
	 */
	public List<CartItemBean> findCartItemsByUserId(Long userId){
		CartBean cart = cartRepository.findCartByUserId(userId);
		if(cart == null) return new ArrayList<>();
		
		return cart.getCartItems();
	}
	
	/**
	 * 以userId取得購物車，並以adId取得推播服務，以加入購物車
	 * @param userId
	 * @param adId
	 * @return
	 */
	public boolean addToCart(Long userId, Long adId) {
		
		// 判斷該用戶是否有該ad
		AdBean ad = adRepository.findAdByAdIdAndUserId(adId, userId);
		if(ad == null) return false;
		
		// 有該ad的話，判斷該用戶是否已有購物車
		CartBean cart = cartRepository.findCartByUserId(userId);
		
		if(cart != null) {
			logger.info("有購物車");
			
			// 判斷購物車中是否有該ad
			Optional<CartItemBean> optional = cartItemRepository.findByAdIdAndCartId(adId, cart.getCartId());
			if(optional.isEmpty()) {
				CartItemBean cartItem = new CartItemBean();
				cartItem.setAdId(adId);
				cartItem.setCartId(cart.getCartId());
				cartItem.setAdtypeId(ad.getAdtypeId());
				cartItem.setAdPrice(ad.getAdPrice());
				
				cartItem.setCart(cart);
				cart.getCartItems().add(cartItem);
				
				cartItemRepository.save(cartItem);
				cartRepository.save(cart);
			}
			
			return true;
			
		}else{	
			logger.info("沒有購物車");
			
			// 建立新的購物車
			CartBean newCart = new CartBean();
			
			newCart.setUserId(userId);
			CartBean savedCart = cartRepository.save(newCart);
			Integer cartId = savedCart.getCartId();
			
			// 增加購物車內容
			CartItemBean cartItem = new CartItemBean();
			cartItem.setAdId(adId);
			cartItem.setCartId(cartId);
			cartItem.setAdtypeId(ad.getAdtypeId());
			cartItem.setAdPrice(ad.getAdPrice());
			
			cartItem.setCart(newCart);
			newCart.getCartItems().add(cartItem);

			cartItemRepository.save(cartItem);
			
			return true;
		}
	}
	
	public boolean deleteCartItem(Long userId, Long adId) {
		
		// 查詢該用戶的購物車
		CartBean cart = cartRepository.findCartByUserId(userId);
		if (cart == null) {
			logger.info("沒有購物車");
			return false;
		}
	    
		// 查詢購物車中的項目
	    Optional<CartItemBean> optional = cartItemRepository.findByAdIdAndCartId(adId, cart.getCartId());
		if(optional.isEmpty()) return false;
		
	    cartItemRepository.deleteByAdId(adId);
		
		Optional<CartItemBean> deletedCartItem = cartItemRepository.findByAdIdAndCartId(adId, cart.getCartId());
	    if (deletedCartItem.isEmpty()) return true;

		return false;
	}
	

	public boolean deleteCartItems(Long userId) {
		CartBean cart = cartRepository.findCartByUserId(userId);
		if (cart == null) return false;
		
		List<CartItemBean> cartItems = cartItemRepository.findByCartId(cart.getCartId());
	    if (cartItems.isEmpty()) return false;
	    
	    cartItemRepository.deleteAll(cartItems);
	    
	    // 驗證
	    List<CartItemBean> deletedCartItems = cartItemRepository.findByCartId(cart.getCartId());
	    if (deletedCartItems.isEmpty()) return true;

	    return false;
	}
	
	
	public boolean deleteCartItems(Integer cartId) {
		Optional<CartBean> optional = cartRepository.findById(cartId);
		if (optional.isEmpty()) return false;
		
		List<CartItemBean> cartItems = cartItemRepository.findByCartId(cartId);
	    if (cartItems.isEmpty()) return false;
	    
	    cartItemRepository.deleteAll(cartItems);
	    
	    // 驗證
	    List<CartItemBean> deletedCartItems = cartItemRepository.findByCartId(optional.get().getCartId());
	    if (deletedCartItems.isEmpty()) return true;

	    return false;
	}
	
	public boolean deleteCart(Long userId) {
		CartBean cart = cartRepository.findCartByUserId(userId);
		if (cart == null) return false;
	    
	    cartRepository.delete(cart);
	    
	    // 驗證
	    Optional<CartBean> deletedCart = cartRepository.findById(cart.getCartId());
	    if (deletedCart.isEmpty()) return true;

	    return false;
	}
	
	/**
	 * 定時刪除購物車
	 * @param userId
	 * @param cartId
	 * @return
	 */
	@Scheduled(cron = "0 0 0 * * *")
	public boolean cleanUpCart() {
		
		logger.info("啟動自動清除購物車");
		
		LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
		List<CartBean> carts = cartRepository.findCartLeftOverThreeDays(threeDaysAgo);
		
		for(CartBean cart : carts) {
			Integer cartId = cart.getCartId();
			
			boolean sucess = deleteCartItems(cartId);
			
			if(!sucess) return false; 
			
			cartRepository.deleteByCartId(cartId);
			
			Optional<CartBean> optional = cartRepository.findById(cartId);
			if(optional.isPresent()) return false;
		}
		
		return true;
	}
	
	/**
	 * 取得使用者持有的優惠券數量
	 * @param userId
	 * @return
	 */
	public Byte getUserCouponNumber(Long userId) {
		return userRepository.getCouponNumber(userId);
	}
	
	
	public int removeOneCoupon(Long userId) {
		return userRepository.removeOneCoupon(userId);
	}
}
