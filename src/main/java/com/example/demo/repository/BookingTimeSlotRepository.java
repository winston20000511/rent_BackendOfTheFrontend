package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.HouseBookingTimeSlotBean;

public interface BookingTimeSlotRepository extends JpaRepository<HouseBookingTimeSlotBean, Long> {

}
