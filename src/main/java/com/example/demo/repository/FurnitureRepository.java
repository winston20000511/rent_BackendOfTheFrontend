package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.FurnitureTableBean;

public interface FurnitureRepository extends JpaRepository<FurnitureTableBean, Long> {
	
}
