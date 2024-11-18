package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.AdBean;

public interface AdRepository extends JpaRepository<AdBean, Long>{

	@Query("From AdBean where userId = :userId")
	public List<AdBean> findAdsByUserIdAndPage(Integer userId, Pageable pageable);
	
	
}
