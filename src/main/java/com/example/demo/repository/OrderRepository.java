package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.OrderBean;

public interface OrderRepository extends JpaRepository<OrderBean, String>{

}
