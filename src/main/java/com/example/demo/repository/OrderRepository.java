package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.OrderBean;

import jakarta.transaction.Transactional;

public interface OrderRepository extends JpaRepository<OrderBean, String>, JpaSpecificationExecutor<OrderBean>{
	
	@Query("From OrderBean where userId = :userId")
	public List<OrderBean> findOrdersByUserIdAndPageNumber(Long userId, Pageable pageable);
	
	@Query("From OrderBean where merchantTradNo = :merchantTradNo")
	public OrderBean findByMerchantTradNo(String merchantTradNo);
	
	@Query("SELECT o.merchantTradNo FROM OrderBean o WHERE o.merchantTradDate BETWEEN :startOfDay AND :endOfDay ORDER BY o.merchantTradDate DESC")
	public Optional<String> findLatestMerchantTradNoByDate(LocalDateTime startOfDay, LocalDateTime endOfDay);
	
}
