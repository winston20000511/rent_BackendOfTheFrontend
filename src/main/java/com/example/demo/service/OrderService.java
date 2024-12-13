package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.dto.OrderConfirmationResponseDTO;
import com.example.demo.dto.OrderResponseDTO;
import com.example.demo.model.AdBean;
import com.example.demo.model.CartBean;
import com.example.demo.model.CartItemBean;
import com.example.demo.model.OrderBean;
import com.example.demo.model.UserTableBean;
import com.example.demo.repository.AdRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.OrderSpecification;
import com.example.demo.repository.UserRepository;

@Service
public class OrderService {

	private OrderRepository orderRepository;
	private AdRepository adRepository;
	private CartRepository cartRepository;
	private CartItemRepository cartItemRepository;
	private UserRepository userRepository;

	@Autowired
	public OrderService(OrderRepository orderRepository, AdRepository adRepository, 
			CartRepository cartRepository, CartItemRepository cartItemRepository, UserRepository userRepository) {
		this.orderRepository = orderRepository;
		this.adRepository = adRepository;
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
		this.userRepository = userRepository;
	}

	// 查詢
	// get orders by conditions and page
	public Page<OrderResponseDTO> findOrdersByConditions(Map<String, String> conditions) {
		Integer pageNumber = Integer.parseInt(conditions.get("page"));

		Specification<OrderBean> spec = OrderSpecification.filter(conditions);

		Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.Direction.DESC, "merchantTradNo");
		Page<OrderBean> page = orderRepository.findAll(spec, pageable);

		List<OrderBean> orders = page.getContent();
		for (OrderBean order : orders) {
			System.out.println("order id: " + order.getMerchantTradNo());
		}

		List<OrderResponseDTO> responseDTOs = setOrderDetailsResponseDTOs(page.getContent());

		return new PageImpl<>(responseDTOs, pageable, page.getTotalElements());
	}

	// get order details by merchantTradNo
	public OrderResponseDTO findOrdersByMerchantTradNo(String merchantTradNo) {
		System.out.println(merchantTradNo);
		OrderBean order = orderRepository.findByMerchantTradNo(merchantTradNo);
		OrderResponseDTO dto = new OrderResponseDTO();

		dto.setMerchantTradNo(merchantTradNo);
		List<AdBean> ads = order.getAds(); // 物件內容（複數）
		List<String> houseTitles = new ArrayList<>(); // 廣告種類（複數）
		List<String> adtypes = new ArrayList<>();
		List<Integer> prices = new ArrayList<>();
		List<Long> adIds = new ArrayList<>();
		for (AdBean ad : ads) {
			houseTitles.add(ad.getHouse().getTitle());
			adtypes.add(ad.getAdtype().getAdName());
			prices.add(ad.getAdPrice());
			adIds.add(ad.getAdId());
		}

		dto.setHouseTitles(houseTitles);
		dto.setAdtypes(adtypes);
		dto.setPrices(prices);
		dto.setAdIds(adIds);

		dto.setTotalAmount(order.getTotalAmount());
		dto.setMerchantTradDate(order.getMerchantTradDate());
		dto.setOrderStatus(order.getOrderStatus());
		dto.setChoosePayment(order.getChoosePayment());

		return dto;
	}
	

	// 新增
	public OrderResponseDTO createOrder(Integer cartId, String paymentMethod) {
		
		System.out.println("cartId: " + cartId + " payment method: " + paymentMethod);
		
		OrderBean newOrder = new OrderBean();
		// orderBean 設定: userId, merchantTradNo, merchantTradDate, 
		// totalAmount, itemName, orderStatus, choosePayment, checkMacValue
		// 關聯: user, ads
			
		// 得到cart的內容
		List<CartItemBean> cartItems = cartItemRepository.findByCartId(cartId);
		List<Long> adIds = new ArrayList<>();
		Long totalAmount = 0L;
		for (CartItemBean cartItem : cartItems) {
			adIds.add(cartItem.getAdId());
			totalAmount += cartItem.getAdPrice();
		}
		System.out.println("adIds: " + adIds);
		System.out.println("totalAmount: " + totalAmount);
		newOrder.setTotalAmount(totalAmount);
		
		// user id: 確定http的參數名稱
		// newOrder.setUserId(Long.parseInt(httpSession.getAttribute("userId")));
		CartBean cart = cartRepository.findById(cartId).get();
		Long userId = cart.getUserId();
		newOrder.setUserId(userId);
		
		// user: 用id找到user 加入
		// UserTableBean user = userRepo.findById();
		// newOrder.setUser(user);
		UserTableBean user = userRepository.findById(userId).get();
		newOrder.setUser(user);
		
		// merchantTradNo: 之後帶自己寫的生成程式
		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
		// 使用訂單生成程式碼帶入
		newOrder.setMerchantTradNo(uuId);
		
		// merchantTradDate
		LocalDateTime date = LocalDateTime.now();
		newOrder.setMerchantTradDate(date);
		
		// totalAmount
		List<AdBean> ads = adRepository.findAllById(adIds);
		for(AdBean ad : ads) {
			System.out.println("ad price: " + ad.getAdPrice());
			ad.setIsPaid(true);
			ad.setOrderId(uuId);
			ad.setPaidDate(date);
		}
		adRepository.saveAll(ads);
		
		// 應該要先0，有取得金流的確認回傳值才改成1
		newOrder.setOrderStatus((short)1);
		newOrder.setTradeDesc("宣傳廣告");
		newOrder.setChoosePayment(paymentMethod);
		newOrder.setCheckMacValue("test");
		
		newOrder.setAds(ads);
		
		OrderBean savedOrder = orderRepository.save(newOrder);
		
		for (CartItemBean cartItem : cartItems) {
			System.out.println("cart item cart id: " + cartItem.getCartId());
		    cartItemRepository.delete(cartItem);
		    Long adId = cartItem.getAdId();
		    cartItemRepository.findById(adId).orElse(null);
		}
		cartRepository.deleteById(cartId);
		
		OrderResponseDTO responseDTO = setOrderDetailsResponseDTO(savedOrder);
		return responseDTO;
	}

	// 取消（變更）
	public boolean cancelOrderByMerchantTradNo(String merchantTradNo) {
		Optional<OrderBean> optional = orderRepository.findById(merchantTradNo);
		if (optional != null) {
			OrderBean order = optional.get();
			order.setOrderStatus((short) 2); // 確認後方可取消
			orderRepository.save(order);
			return true;
		}

		return false;
	}

	// 給使用者確認訂單付款內容
	public List<OrderConfirmationResponseDTO> getOrderConfirmationResponseDTOsByCartId(Integer cartId) {

		List<CartItemBean> cartItems = cartItemRepository.findByCartId(cartId);
		List<OrderConfirmationResponseDTO> responseDTOs = new ArrayList<>();

		for (CartItemBean cartItem : cartItems) {
			OrderConfirmationResponseDTO dto = new OrderConfirmationResponseDTO();
			AdBean ad = cartItem.getAd();
			dto.setHouseTitle(ad.getHouse().getTitle());
			dto.setAdId(cartItem.getAdId());

			LocalDateTime startDate = LocalDateTime.now();
			String adName = ad.getAdtype().getAdName();
			Long days = Long.parseLong(adName.replaceAll("[^0-9]", ""));
			LocalDateTime endDate = startDate.plusDays(days);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String period = startDate.format(formatter) + "~" + endDate.format(formatter);
			dto.setAdPeriod(period);

			dto.setAdPrice(cartItem.getAdPrice());
			dto.setAdName(cartItem.getAdtype().getAdName());

			responseDTOs.add(dto);
		}

		return responseDTOs;
	}

	/* private method */
	private List<OrderResponseDTO> setOrderDetailsResponseDTOs(List<OrderBean> orders) {

		List<OrderResponseDTO> responseDTOs = new ArrayList<>();

		for (OrderBean order : orders) {
			OrderResponseDTO responseDTO = new OrderResponseDTO();

			List<AdBean> ads = order.getAds();
			List<Long> adIds = new ArrayList<>();
			List<String> adtypes = new ArrayList<>();
			List<String> houseTitles = new ArrayList<>();
			for (AdBean ad : ads) {
				adIds.add(ad.getAdId());
				adtypes.add(ad.getAdtype().getAdName());
				houseTitles.add(ad.getHouse().getTitle());
			}

			responseDTO.setAdIds(adIds);
			responseDTO.setAdtypes(adtypes);
			responseDTO.setHouseTitles(houseTitles);

			responseDTO.setMerchantTradDate(order.getMerchantTradDate());
			responseDTO.setMerchantTradNo(order.getMerchantTradNo());
			responseDTO.setOrderStatus(order.getOrderStatus());
			responseDTO.setTotalAmount(order.getTotalAmount());

			responseDTOs.add(responseDTO);
		}

		return responseDTOs;
	}

	private OrderResponseDTO setOrderDetailsResponseDTO(OrderBean order) {

		OrderResponseDTO responseDTO = new OrderResponseDTO();

		List<AdBean> ads = order.getAds();
		List<Long> adIds = new ArrayList<>();
		List<String> adtypes = new ArrayList<>();
		List<String> houseTitles = new ArrayList<>();
		List<Integer> adPrices = new ArrayList<>();
		for (AdBean ad : ads) {
			adIds.add(ad.getAdId());
			adtypes.add(ad.getAdtype().getAdName());
			adPrices.add(ad.getAdPrice());
			houseTitles.add(ad.getHouse().getTitle());
		}

		responseDTO.setAdIds(adIds);
		responseDTO.setAdtypes(adtypes);
		responseDTO.setPrices(adPrices);
		responseDTO.setHouseTitles(houseTitles);

		System.out.println("order.getChoosePayment() " + order.getChoosePayment());
		responseDTO.setChoosePayment(order.getChoosePayment());
		responseDTO.setMerchantTradDate(order.getMerchantTradDate());
		responseDTO.setMerchantTradNo(order.getMerchantTradNo());
		responseDTO.setOrderStatus(order.getOrderStatus());
		responseDTO.setTotalAmount(order.getTotalAmount());

	return responseDTO;
}

}
