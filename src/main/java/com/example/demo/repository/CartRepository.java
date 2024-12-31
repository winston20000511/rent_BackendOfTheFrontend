package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.CartBean;

@Repository
public interface CartRepository extends JpaRepository<CartBean, Integer>{

	@Query("From CartBean where userId = :userId")
	CartBean findCartByUserId(Long userId);

	@Query("From CartBean c where c.createdAt < :threeDaysAgo")
	List<CartBean> findCartLeftOverThreeDays(LocalDateTime threeDaysAgo);
	
	@Modifying
	@Query("Delete from CartBean c where c.cartId = :cartId")
	void deleteByCartId(@Param("cartId") Integer cartId);
}
