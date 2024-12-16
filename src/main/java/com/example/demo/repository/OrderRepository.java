package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.OrderBean;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public interface OrderRepository extends JpaRepository<OrderBean, String>, JpaSpecificationExecutor<OrderBean>{
	
	@Query("From OrderBean where userId = :userId")
	public List<OrderBean> findOrdersByUserIdAndPageNumber(Long userId, Pageable pageable);
	
	@Query("From OrderBean where merchantTradNo = :merchantTradNo")
	public OrderBean findByMerchantTradNo(String merchantTradNo);
}
