package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.CartItemBean;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemBean, Long>{

	@Query("From CartItemBean where adId = :adId and cartId = :cartId")
	Optional<CartItemBean> findByAdIdAndCartId(Long adId, Integer cartId);
	
	@Query("From CartItemBean where cartId = :cartId")
	List<CartItemBean> findByCartId(Integer cartId);
	
	@Query("Select ci.adId from CartItemBean ci join ci.cart c where c.userId = :userId")
	Optional<List<Long>> findAdIdsByUserId(Long userId);
	
	@Modifying
	@Query("Delete from CartItemBean c where c.adId = :adId")
	void deleteCartItemsByAdId(@Param("adId") Long adId);
	
	@Modifying
	@Query("Delete from CartItemBean c where c.cartId = :cartId")
	void deleteCartItemsByCartId(@Param("cartId") Integer cartId);
	
}
