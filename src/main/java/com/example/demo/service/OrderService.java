package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AdDetailsResponseDTO;
import com.example.demo.dto.OrderResponseDTO;
import com.example.demo.dto.OrderSearchRequestDTO;
import com.example.demo.model.AdBean;
import com.example.demo.model.AdtypeBean;
import com.example.demo.model.OrderBean;
import com.example.demo.repository.AdSpecification;
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

		List<OrderResponseDTO> responseDTOs = setOrderDetailsResponsesDTO(page.getContent());

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
		for(AdBean ad : ads) { 
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

	// 取消（變更）
	public boolean cancelOrderByMerchantTradNo(String merchantTradNo) {
		Optional<OrderBean> optional = orderRepository.findById(merchantTradNo);
		if(optional != null) {
			OrderBean order = optional.get();
			order.setOrderStatus((short)2); // 確認後方可取消
			orderRepository.save(order);
			return true;
		}
		
		return false;
	}

	/* private method */
	private List<OrderResponseDTO> setOrderDetailsResponsesDTO(List<OrderBean> orders) {

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

	/* ecpay */
	public String ecpayCheckout() {
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
