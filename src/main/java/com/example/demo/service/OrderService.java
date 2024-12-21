package com.example.demo.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.dto.OrderConfirmationResponseDTO;
import com.example.demo.dto.OrderCreationRequestDTO;
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

	private Logger logger = Logger.getLogger(OrderService.class.getName());

	private OrderRepository orderRepository;
	private AdRepository adRepository;
	private CartRepository cartRepository;
	private CartItemRepository cartItemRepository;
	private UserRepository userRepository;

	private SerialOrderNoService serialNoService;

	@Autowired
	public OrderService(OrderRepository orderRepository, AdRepository adRepository, CartRepository cartRepository,
			CartItemRepository cartItemRepository, UserRepository userRepository,
			SerialOrderNoService serialNoService) {
		this.orderRepository = orderRepository;
		this.adRepository = adRepository;
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
		this.userRepository = userRepository;
		this.serialNoService = serialNoService;
	}

	/**
	 * 以前端送入的篩選條件搜尋訂單資料
	 * 
	 * @return Page<OrderResponseDTO>
	 */
	public Page<OrderResponseDTO> findOrdersByConditions(
			Long userId, Integer pageNumber, String orderStatus, String dateRange, String inputCondition, String userInput) {

		Specification<OrderBean> spec = OrderSpecification.filter(userId, pageNumber, orderStatus, dateRange, inputCondition, userInput);

		Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.Direction.DESC, "merchantTradNo");
		Page<OrderBean> page = orderRepository.findAll(spec, pageable);

		List<OrderBean> orders = page.getContent();
		for (OrderBean order : orders) {
			logger.severe("order id: " + order.getMerchantTradNo());
		}

		List<OrderResponseDTO> responseDTOs = setOrderDetailsResponseDTOs(page.getContent());

		return new PageImpl<>(responseDTOs, pageable, page.getTotalElements());
	}

	/**
	 * 以訂單號碼取得訂單資料
	 * 
	 * @param merchantTradNo
	 * @return OrderResponseDTO 訂單詳細資料
	 */
	public OrderResponseDTO findOrdersByMerchantTradNo(String merchantTradNo) {
		logger.severe(merchantTradNo);
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

	/**
	 * 在資料庫建立新訂單，OrderBean 設定: userId, merchantTradNo, merchantTradDate,
	 * totalAmount, itemName, orderStatus, choosePayment, checkMacValue
	 * 
	 * @param cartId
	 * @param paymentMethod
	 * @return OrderResponseDTO 訂單詳細資料
	 */
	public OrderResponseDTO createOrder(Long userId, OrderCreationRequestDTO requestDTO) {
	    logger.info(requestDTO.toString());

	    // 檢驗該cart是該user的
	    CartBean cart = cartRepository.findById(requestDTO.getCartId()).orElse(null);
	    if (cart == null || !Objects.equals(cart.getUserId(), userId)) {
	        return null;
	    }

	    OrderBean newOrder = new OrderBean();

	    List<CartItemBean> cartItems = cartItemRepository.findByCartId(requestDTO.getCartId());
	    Set<Long> appliedAdIds = new HashSet<>(requestDTO.getCouponApplied());

	    // 計算總金額並更新商品價格
	    Long totalAmount = 0L;
	    List<Long> adIds = new ArrayList<>();
	    for (CartItemBean cartItem : cartItems) {
	        adIds.add(cartItem.getAdId());
	    }
	    
	    List<AdBean> ads = adRepository.findAllById(adIds);

	    // 更新商品價格並計算總金額
	    for (AdBean ad : ads) {
	        boolean isCouponApplied = appliedAdIds.contains(ad.getAdId());
	        int discount = isCouponApplied ? calculateDiscount(ad.getAdPrice()) : 0;
	        totalAmount += ad.getAdPrice() - discount;

	        ad.setAdPrice(ad.getAdPrice() - discount);
	        ad.setIsCouponUsed(isCouponApplied ? 1 : 0);
	        ad.setIsPaid(true);
	        ad.setOrderId(newOrder.getMerchantTradNo());
	        ad.setPaidDate(newOrder.getMerchantTradDate());

	        if (isCouponApplied) {
	            ad.setAdPrice(ad.getAdPrice() - discount);
	        }

	        newOrder.setItemName(ad.getAdtype().getAdName());
	    }

	    adRepository.saveAll(ads);

	    newOrder.setTotalAmount(totalAmount);
	    newOrder.setUserId(userId);
	    UserTableBean user = userRepository.findById(userId).orElse(null);
	    if (user == null) return null;
	    
	    newOrder.setUser(user);

	    String merchantTradNo = serialNoService.generateSerialNumber();
	    LocalDateTime date = LocalDateTime.now();
	    newOrder.setMerchantTradNo(merchantTradNo);
	    newOrder.setMerchantTradDate(date);

	    newOrder.setOrderStatus((short) 0);
	    newOrder.setTradeDesc("宣傳廣告");
	    newOrder.setChoosePayment(requestDTO.getChoosePayment());
	    newOrder.setThirdParty(requestDTO.getThirdParty());
	    newOrder.setAds(ads);

	    OrderBean savedOrder = orderRepository.save(newOrder);
	    
	    // 檢驗有沒有去掉
	    userRepository.removeOneCoupon(userId);

	    OrderResponseDTO responseDTO = setOrderDetailsResponseDTO(savedOrder);
	    return responseDTO;
	}
	
	private int calculateDiscount(int adPrice) {
	    BigDecimal discount = new BigDecimal(adPrice * 0.1);
	    BigDecimal discountInt = discount.setScale(0, RoundingMode.DOWN);  // 無條件捨去
	    return discountInt.intValue();
	}

	/**
	 * 申請取消訂單: 將資料庫中order status設為2
	 * 
	 * @param merchantTradNo
	 * @return boolean 提出申請就設為 2 = 待平台方確認
	 */
	public boolean cancelOrderByMerchantTradNo(String merchantTradNo) {
		Optional<OrderBean> optional = orderRepository.findById(merchantTradNo);
		if (optional.isEmpty()) {
			return false;
		}

		OrderBean order = optional.get();
		order.setOrderStatus((short) 2); // 確認後方可取消
		orderRepository.save(order);
		return true;
	}

	/**
	 * 在付款頁面中，顯示給客人確認的訂單內容
	 * 
	 * @param cartId
	 * @return OrderConfirmationResponseDTO 確認用的訂單內容
	 */
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

	/**
	 * 設置多筆訂單詳細資料的DTO
	 * 
	 * @param orders
	 * @return OrderResponseDTO
	 */
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

	/**
	 * 設置單筆訂單詳細資料的DTO
	 * 
	 * @param order
	 * @return OrderResponseDTO
	 */
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
