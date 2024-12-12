package com.example.demo.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.CartBean;

@Repository
public interface CartRepository extends JpaRepository<CartBean, Integer>{

	@Query("From CartBean where userId = :userId")
	public CartBean findCartByUserId(Long userId);
}
