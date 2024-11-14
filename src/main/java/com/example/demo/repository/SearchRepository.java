package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.AddressTableBean;


@Repository
public interface SearchRepository extends JpaRepository<AddressTableBean, Long>{
	
	List<AddressTableBean> findByCity(String city);
	
	@Query("from Address where concat(city ,concat(township,street)) like %:n%")
	List<AddressTableBean> findByKeyWord(@Param("n") String name);
}
