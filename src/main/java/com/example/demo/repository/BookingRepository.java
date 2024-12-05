package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.dto.BookingDetailDTO;
import com.example.demo.dto.HouseOwnerDetailDTO;
import com.example.demo.model.BookingBean;

public interface BookingRepository extends JpaRepository<BookingBean, Long> {
	
	@Query(value = "SELECT h.house_id, u.email FROM  house_table h JOIN user_table u ON h.user_id = u.user_id WHERE h.user_id = :houseId", nativeQuery = true)
	HouseOwnerDetailDTO getOwnerDetailByHouseId(Long houseId);
	
}
