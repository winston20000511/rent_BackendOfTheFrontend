package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.AddressDTO;
import com.example.demo.model.HouseTableBean;


@Repository
public interface SearchRepository extends JpaRepository<HouseTableBean, Long>{
	
//	List<Address> findByCity(String city);
//	
	@Query("from HouseTableBean where address like %:n%")
	List<HouseTableBean> findByKeyWord(@Param("n") String name);

	@Query("select h.address , h.lat , h.lng , h.price from HouseTableBean h where address like :n%")
	List<HouseTableBean> findByCityAndTownship(@Param("n") String name);
	
}
