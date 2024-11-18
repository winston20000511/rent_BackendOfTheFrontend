package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.OrderBean;

public interface OrderRepository extends JpaRepository<OrderBean, String>{

	@Query("From OrderBean where userId = :userId")
	public List<OrderBean> findOrdersByUserIdAndPageNumber(Integer userId, Pageable pageable);
	
	@Query("From OrderBean where userId = :userId and merchantTradNo = :merchantTradNo")
	public List<OrderBean> findOrdersByUserIdAndMerchantTradNo(Integer userId, String merchantTradNo);
	
	public OrderBean findByMerchantTradNo(String merchantTradNo);
}
