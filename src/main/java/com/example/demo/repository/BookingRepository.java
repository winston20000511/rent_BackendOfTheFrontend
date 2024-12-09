package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.BookingBean;
import com.example.demo.model.BookingId;

public interface BookingRepository extends JpaRepository<BookingBean, BookingId> {
	
	
}
