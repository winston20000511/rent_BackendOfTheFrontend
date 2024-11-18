package com.example.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingTimeSlotRepository extends JpaRepository<HouseBookingTimeSlotBean, Long> {

}
