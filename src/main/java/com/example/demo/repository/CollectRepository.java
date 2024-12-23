package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.CollectTableBean;

import jakarta.transaction.Transactional;

@Repository
public interface CollectRepository extends JpaRepository<CollectTableBean, Long> {
	// 根據USERID列出USER收藏房屋的ID清單
	@Query("SELECT c.house.houseId FROM CollectTableBean c WHERE c.user.userId = :userId")
	List<Long> findHouseIdsByUserId(@Param("userId") Long userId);

	@Modifying
	@Transactional
	@Query("DELETE FROM CollectTableBean c WHERE c.user.userId = :userId AND c.house.houseId = :houseId")
	void deleteByUserIdAndHouseId(@Param("userId") Long userId, @Param("houseId") Long houseId);
	
	boolean existsByUser_UserIdAndHouse_HouseId(Long userId, Long houseId);
  
}