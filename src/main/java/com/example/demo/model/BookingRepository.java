package com.example.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<BookingBean, BookingId> {

}
