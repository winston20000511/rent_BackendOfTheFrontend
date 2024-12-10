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
	//根據USERID列出USER收藏房屋的ID清單
	 @Query("SELECT c.house.houseId FROM CollectTableBean c WHERE c.user.userId = :userId")
	    List<Long> findHouseIdsByUserId(@Param("userId") Long userId);
	
	 //根據HOUSEID跟USERID刪除該USER的收藏房屋
    @Transactional
    @Modifying
    @Query("DELETE FROM CollectTableBean c WHERE c.house.houseId = :houseId")
    void deleteByHouseId(@Param("houseId") Long houseId);
}