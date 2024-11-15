package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.OrderBean;
import com.example.demo.repository.OrderRepository;

@Service
public class OrderService {
	
	private OrderRepository orderRepository;
	
	@Autowired
	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}
	
	// get the oder list by user id
	
	// get an order by 
	
	// create a new order
	public OrderBean createOrder(OrderBean orderBean) {
		OrderBean newOrder = orderRepository.save(orderBean);
		return newOrder;
	}
	
	// cancel(=update) a order
	public boolean cancelOrderById(String orderId) {
		Optional<OrderBean> option = orderRepository.findById(orderId);
		if(option.isPresent()) {
			OrderBean dbOrder = option.get();
			orderRepository.delete(dbOrder);
			return true;
		}
		
		return false;
	}
}
