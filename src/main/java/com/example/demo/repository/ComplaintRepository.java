package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ComplaintBean;

public interface ComplaintRepository extends JpaRepository<ComplaintBean, Integer> {
	

}
