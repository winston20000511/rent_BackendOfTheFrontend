package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.AdBean;

public interface AdRepository extends JpaRepository<AdBean, Integer>{

	Page<AdBean> findAllAdsByPage(Integer pageNumber, PageRequest pageRequest);
}
