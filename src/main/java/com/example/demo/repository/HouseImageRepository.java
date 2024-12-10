package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.HouseImageTableBean;

public interface HouseImageRepository extends JpaRepository<HouseImageTableBean, Long> {
	@Query("SELECT h FROM HouseImageTableBean h WHERE h.house.id = :houseId")
	List<HouseImageTableBean> findByHouseId(@Param("houseId") Long houseId);


}