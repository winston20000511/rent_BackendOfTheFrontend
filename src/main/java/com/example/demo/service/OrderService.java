package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.dto.OrderResponseDTO;
import com.example.demo.dto.OrderSearchRequestDTO;
import com.example.demo.model.OrderBean;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.OrderSpecification;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;

@Service
public class OrderService {
	
	private OrderRepository orderRepository;
	
	@Autowired
	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}
	
	// get orders by page
	public List<OrderBean> findOrdersByUserIdAndPageNumber(Long userId, Integer pageNumber){
		Pageable pageable = PageRequest.of(pageNumber-1, 10, Sort.Direction.DESC, "merchantTradDate");
		List<OrderBean> orders = orderRepository.findOrdersByUserIdAndPageNumber(userId, pageable);
		return orders;
	}
	
	// get orders by conditions and page
	// 要加分頁
	public List<OrderResponseDTO> findOrdersByConditions(OrderSearchRequestDTO requestDTO){
		
		System.out.println(requestDTO);
		
		Specification<OrderBean> spec = OrderSpecification.filter(requestDTO);
		
		List<OrderBean> orders = orderRepository.findAll(spec);
		
		List<OrderResponseDTO> responseDTOList = new ArrayList<>();
		
		for(OrderBean order : orders) {
			OrderResponseDTO responseDTO = new OrderResponseDTO();
			responseDTO.setMerchantTradNo(order.getMerchantTradNo());
			responseDTO.setOrderStatus(order.getOrderStatus() == 0? "已取消" : "一般訂單");
			
			List<String> houseTitles = new ArrayList<>();
			for(int i = 0; i< order.getAds().size(); i++) {
				String title = order.getAds().get(i).getHouse().getTitle();
				houseTitles.add(title);
			}
		    responseDTO.setHouseTitles(houseTitles);
		    responseDTO.setMerchantTradDate(order.getMerchantTradDate());
		    
		    responseDTOList.add(responseDTO);

		}
		
		System.out.println("responseDTOList: " + responseDTOList);
		
		return responseDTOList;
	}
	
	
	// create a new order
	public OrderBean createOrder(OrderBean orderBean) {
		OrderBean newOrder = orderRepository.save(orderBean);
		return newOrder;
	}
	
	// cancel(=update) a order
	public OrderBean cancelOrderByMerchantTradNo(String merchantTradNo) {
		Optional<OrderBean> option = orderRepository.findById(merchantTradNo);
		if(option.isPresent()) {
			OrderBean dbOrder = option.get();
			dbOrder.setOrderStatus((short) 0);
			OrderBean savedOrder = orderRepository.save(dbOrder);
			return savedOrder;
		}
		
		return null;
	}

	/* ecpay */
	public String ecpayCheckout(){
		AllInOne all = new AllInOne("");
		AioCheckOutALL obj = new AioCheckOutALL();
		obj.setMerchantTradeNo("textCompany0100");
		obj.setMerchantTradeDate("2024/11/18 08:05:23");
		obj.setTotalAmount("50");
		obj.setTradeDesc("test Description");
		obj.setItemName("TestItem");
		// 交易結果回傳網址，只接受 https 開頭的網站，可以使用 ngrok
		obj.setReturnURL("<http://211.23.128.214:5000>");
		obj.setNeedExtraPaidInfo("N");
		// 商店轉跳網址
		obj.setClientBackURL("<http://192.168.1.37:8080/>");
		String form = all.aioCheckOut(obj, null);
		return form;
	}
	
	// get ecpay check value and verify
	
}
