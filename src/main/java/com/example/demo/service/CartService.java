package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.AdBean;
import com.example.demo.model.CartBean;
import com.example.demo.model.CartItemBean;
import com.example.demo.repository.AdRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.UserRepository;

@Service
public class CartService {
	
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
	
	// 取得購物車內的廣告ID
	public List<Long> getCartItems(Long userId){
		Optional<List<Long>> optional = cartItemRepository.findAdIdsByUserId(userId);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		
		return null;
	}
	
	// 取得購物車內容
	public List<CartItemBean> findCartItemsByUserId(Long userId){
		CartBean cart = cartRepository.findCartByUserId(userId);
		if(cart == null) return null;
		
		return cart.getCartItems();
	}
	
	// 新增購物車內容
	public boolean addToCart(Long adId, Long userId) {
		
		// 判斷該用戶有該ad
		AdBean ad = adRepository.findAdByAdIdAndUserId(adId, userId);
		if(ad == null) {
			return false;
		}
		
		// 有該ad的話，判斷該用戶是否已有購物車
		CartBean cart = cartRepository.findCartByUserId(userId);
		
		if(cart != null) {
			System.out.println("有購物車");
			
			boolean itemExists = cart.getCartItems().stream().anyMatch(item -> item.getAdId().equals(adId));
			if(itemExists) {
				System.out.println("購物車中有該廣告了");
				return false;
			}
			
			CartItemBean cartItem = new CartItemBean();
			cartItem.setAdId(adId);
			cartItem.setCartId(cart.getCartId());
			cartItem.setAdtypeId(ad.getAdtypeId());
			cartItem.setAdPrice(ad.getAdPrice());
			
			cartItem.setCart(cart);
			cart.getCartItems().add(cartItem);
			
			cartItemRepository.save(cartItem);
			cartRepository.save(cart);
			
			return true;
			
		}else{	
			System.out.println("沒有購物車");
			
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
	
	@Transactional
	public boolean deleteCartItem(Long adId, Long userId) {
		
		// 查詢該用戶的購物車
		System.out.println("userId: " + userId);
		CartBean cart = cartRepository.findCartByUserId(userId);
		if (cart == null) return false;
		System.out.println("user id: " + userId + " get cart id: " + cart.getCartId());
	    
		// 查詢購物車中的項目
	    Optional<CartItemBean> optional = cartItemRepository.findByAdIdAndCartId(adId, cart.getCartId());
		if(optional.isEmpty()) {
			return false;
		}
		
		System.out.println(optional.get().getAdId());
	    
		if (optional.isPresent()) {
	        cartItemRepository.deleteByAdId(adId);
	    }
		
		Optional<CartItemBean> deletedCartItem = cartItemRepository.findByAdIdAndCartId(adId, cart.getCartId());
	    if (deletedCartItem.isEmpty()) {
	        return true;
	    }

		return false;
	}

	public boolean deleteCart(Integer cartId) {
		Optional<CartBean> optional = cartRepository.findById(cartId);
		
		if(optional.isPresent()) {
			cartRepository.deleteById(cartId);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 取得使用者持有的優惠券數量
	 * @param userId
	 * @return
	 */
	public Byte getUserCouponNumber(Long userId) {
		return userRepository.getCouponNumber(userId);
	}
}
