package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ConditionTableBean;

public interface ConditionRepository extends JpaRepository<ConditionTableBean, Long> {
	
}