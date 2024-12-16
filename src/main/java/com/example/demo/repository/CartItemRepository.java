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
	public Optional<CartItemBean> findByAdIdAndCartId(Long adId, Integer cartId);
	
	@Query("From CartItemBean where cartId = :cartId")
	public List<CartItemBean> findByCartId(Integer cartId);
	
	@Query("select ci.adId from CartItemBean ci join ci.cart c where c.userId = :userId")
	public Optional<List<Long>> findAdIdsByUserId(Long userId);
	
	@Modifying
	@Query("delete from CartItemBean c where c.adId = :adId")
	void deleteByAdId(@Param("adId") Long adId);
	
}
